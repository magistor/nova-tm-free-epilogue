package l2p.gameserver.instancemanager;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import l2p.extensions.multilang.CustomMessage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;

import l2p.Config;
import l2p.database.DatabaseUtils;
import l2p.database.FiltredPreparedStatement;
import l2p.database.L2DatabaseFactory;
import l2p.database.ThreadConnection;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.ExBR_BuyProductResult;
import l2p.gameserver.serverpackets.ExBR_ProductInfo;
import l2p.gameserver.serverpackets.ExBR_ProductList;
import l2p.gameserver.serverpackets.ExBR_RecentProductListPacket;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.util.ValueSortMap;

/**
 *
 * @author Nosferatus (c)
 * @version 1.0.1
 * @time 11:35
 */
public final class PrimeShopManager {

    private static final Logger _log = Logger.getLogger(PrimeShopManager.class.getName());
    private static PrimeShopManager _instance;
    public final int BR_BUY_SUCCESS = 1;
    public final int BR_BUY_LACK_OF_POINT = -1;
    public final int BR_BUY_INVALID_PRODUCT = -2;
    public final int BR_BUY_USER_CANCEL = -3;
    public final int BR_BUY_INVENTROY_OVERFLOW = -4;
    public final int BR_BUY_CLOSED_PRODUCT = -5;
    public final int BR_BUY_SERVER_ERROR = -6;
    public final int BR_BUY_BEFORE_SALE_DATE = -7;
    public final int BR_BUY_AFTER_SALE_DATE = -8;
    public final int BR_BUY_INVALID_USER = -9;
    public final int BR_BUY_INVALID_ITEM = -10;
    public final int BR_BUY_INVALID_USER_STATE = -11;
    public final int BR_BUY_NOT_DAY_OF_WEEK = -12;
    public final int BR_BUY_NOT_TIME_OF_DAY = -13;
    public final int BR_BUY_SOLD_OUT = -14;
    public final int MAX_BUY_COUNT = 99;
    public final int CURRENCY_ID = Config.ITEM_MALL_ITEM_ID_FOR_BUY;
    private HashMap<Integer, ItemMallItemTemplate> brTemplates;
    private HashMap<Integer, ItemMallItem> shop;
    protected ExBR_ProductList list;
    private ConcurrentHashMap<Integer, List<ItemMallItem>> recentList;

    public static PrimeShopManager getInstance() {
        if (_instance == null) {
            _instance = new PrimeShopManager();
        }
        return _instance;
    }

    private PrimeShopManager() {
        brTemplates = new HashMap<Integer, ItemMallItemTemplate>();
        shop = new HashMap<Integer, ItemMallItem>();
        list = null;
        recentList = new ConcurrentHashMap<Integer, List<ItemMallItem>>();
        load();
    }

    public void requestBuyItem(L2Player player, int brId, int count) {
        if (count > MAX_BUY_COUNT) {
            count = MAX_BUY_COUNT;
        }
        if (count < 1) {
            count = 1;
        }

        ItemMallItem item = shop.get(brId);
        if (item == null) {
            player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_INVALID_PRODUCT", player));
            return;
        }

        if (player.getInventory().getCountOf(CURRENCY_ID) < item.price * count) {
            player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_LACK_OF_POINT", player));
            return;
        }

        Calendar cal = Calendar.getInstance();
        if (item.iStartSale > 0 && (item.iStartSale > (int) (cal.getTimeInMillis() / 1000))) {
            player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_BEFORE_SALE_DATE", player));
            return;
        }

        if (item.iEndSale > 0 && (item.iEndSale < (int) (cal.getTimeInMillis() / 1000))) {
            player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_AFTER_SALE_DATE", player));
            return;
        }

        if (item.iStartHour != 0 || item.iStartMin != 0 || item.iEndHour != 0 || item.iEndMin != 0) {
            if ((item.iStartHour > cal.get(Calendar.HOUR_OF_DAY) && item.iStartMin > cal.get(Calendar.HOUR_OF_DAY))
                    || (item.iEndHour < cal.get(Calendar.HOUR_OF_DAY) && item.iEndMin < cal.get(Calendar.HOUR_OF_DAY))) {
                player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_NOT_TIME_OF_DAY", player));
                return;
            }
        }

        if (item.isLimited() && (item.limit() || item.iMaxStock - item.iStock < count)) {
            sendResult(player, BR_BUY_SOLD_OUT);
            return;
        }
        player.getInventory().destroyItemByItemId(CURRENCY_ID, item.price * count, true);
        player.sendMessage("Вы потратили " + (item.price * count) + " " + Config.ITEM_MALL_ITEM_NAME);

        L2ItemInstance dummy = new L2ItemInstance(0, item.template.itemId);
        if (dummy.isStackable()) {
            if (!player.getInventory().validateWeight(dummy.getItem().getWeight() * item.count * count)) {
                player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_INVENTROY_OVERFLOW", player));
                return;
            }

            if (player.getInventory().getItemByItemId(item.template.itemId) == null && !player.getInventory().validateCapacity(1)) {
                player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_INVENTROY_OVERFLOW", player));
                return;
            }

            player.getInventory().addItem(item.template.itemId, count);
            player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EARNED_S2_S1S).addItemName(item.template.itemId).addNumber(count));
        } else {
            if (!player.getInventory().validateCapacity(item.count * count) || !player.getInventory().validateWeight(dummy.getItem().getWeight() * item.count * count)) {
                player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_INVENTROY_OVERFLOW", player));
                return;
            }

            for (int i = 0; i < count * item.count; i++) {
                player.getInventory().addItem(item.template.itemId, count);
                player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EARNED_S1).addItemName(item.template.itemId));
            }
        }

        if (item.isLimited()) {
            synchronized (item) {
                item.iStock += count;
            }
        }
        item.iSale += count;
        if (recentList.get(Integer.valueOf(player.getObjectId())) == null) {
            List<ItemMallItem> charList = new ArrayList<ItemMallItem>();
            charList.add(item);
            recentList.put(player.getObjectId(), charList);
        }
        else
        {
            recentList.get(player.getObjectId()).add(item);
        }
        player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_SUCCESS", player));
    }

    public void load() {
        loadTempaltes();
        loadShop();
    }

    public void loadTempaltes() {
        brTemplates = new HashMap<Integer, ItemMallItemTemplate>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);

            final File file = new File(Config.DATAPACK_ROOT + "/data/prime_shop.xml");
            final Document doc = factory.newDocumentBuilder().parse(file);

            for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()) {
                if ("list".equalsIgnoreCase(n.getNodeName())) {
                    for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
                        if ("item".equalsIgnoreCase(d.getNodeName())) {
                            NamedNodeMap attrs = d.getAttributes();

                            int brId = Integer.parseInt(attrs.getNamedItem("brId").getNodeValue());
                            int itemId = 0;
                            try {
                                itemId = Integer.parseInt(attrs.getNamedItem("itemId").getNodeValue());
                            } catch (NumberFormatException e) {
                            }
                            if (itemId == 0) {
                                continue;
                            }

                            int cat = Integer.parseInt(attrs.getNamedItem("category").getNodeValue());

                            ItemMallItemTemplate csit = new ItemMallItemTemplate();
                            csit.brId = brId;
                            csit.itemId = itemId;
                            csit.category = cat;

                            brTemplates.put(csit.itemId, csit);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.severe("ItemMallShop: Error parsing prime_shop.xml file. ");
            e.printStackTrace();
        }

        _log.info("ItemMallShop: loaded " + brTemplates.size() + " item templates.");
    }

    @SuppressWarnings("unchecked")
    public void loadShop() {
        shop = new HashMap<Integer, ItemMallItem>();
        ThreadConnection con = null;
        FiltredPreparedStatement statement = null;
        ResultSet result = null;
        L2ItemInstance dummy;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT * FROM prime_shop WHERE onSale=1 ORDER BY ord");
            result = statement.executeQuery();
            while (result.next()) {
                int vsm = result.getInt("itemId");

                ItemMallItemTemplate template = brTemplates.get(vsm);

                if (template == null) {
                    _log.warning("ItemMallShop: item template for " + vsm + " was not found. skipping.");
                    continue;
                }

                ItemMallItem item = new ItemMallItem(template);
                item.count = result.getInt("count");
                item.price = result.getInt("price");
                item.order = result.getInt("ord");
                item.iCategory2 = result.getInt("iCategory2");
                item.iStartSale = result.getInt("iStartSale");
                item.iEndSale = result.getInt("iEndSale");
                item.iStartHour = result.getInt("iStartHour");
                item.iStartMin = result.getInt("iStartMin");
                item.iEndHour = result.getInt("iEndHour");
                item.iEndMin = result.getInt("iEndMin");
                item.iStock = result.getInt("iStock");
                item.iMaxStock = result.getInt("iMaxStock");

                dummy = new L2ItemInstance(0, vsm);
                item.iWeight = dummy.getItem().getWeight();
                item.iDropable = dummy.getItem().isDropable();
                shop.put(item.template.brId, item);
            }
        } catch (final Exception e) {
            _log.warning("ItemMallShop: error in loadShop() " + e);
        } finally {
            DatabaseUtils.closeDatabaseCSR(con, statement, result);
        }
        _log.info("ItemMallShop: loaded " + shop.size() + " items available for trading.");
        list = new ExBR_ProductList();
        ValueSortMap vsm = new ValueSortMap();
        Map<ItemMallItem, Integer> data = new LinkedHashMap<ItemMallItem, Integer>();

        for (ItemMallItem imi : shop.values()) {
            data.put(imi, imi.order);
        }

        data = vsm.sortMapByValue(data, true);
        list.col = data.keySet();
    }

    public void saveData() {
        ThreadConnection con = null;
        FiltredPreparedStatement statement = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            for (ItemMallItem imi : shop.values()) {
                if (imi.isLimited()) {
                    statement = con.prepareStatement("UPDATE item_mall set iStock=? where ord=?");
                    statement.setInt(1, imi.iStock);
                    statement.setInt(2, imi.order);
                    statement.executeUpdate();
                    statement.close();
                }
            }
            System.out.println("ItemMallShop: Data saved.");
        } catch (final Exception e) {
            _log.warning("ItemMallShop: error in saveData() " + e);
        } finally {
            DatabaseUtils.closeDatabaseCS(con, statement);
        }
    }

    public void sendResult(L2Player player, int code) {
        player.sendPacket(new ExBR_BuyProductResult(code));
    }

    public void showList(L2Player player) {
        player.sendPacket(list);
    }

    public void showItemInfo(L2Player player, int brId) {
        ItemMallItem item = shop.get(brId);
        if (item == null) {
            player.sendMessage(new CustomMessage("l2p.gameserver.instancemanager.PrimeShopManager.BR_BUY_INVALID_ITEM", player));
            return;
        }
        player.sendPacket(new ExBR_ProductInfo(item));
    }

    public void recentProductList(L2Player player)
  {
    player.sendPacket(new ExBR_RecentProductListPacket(player.getObjectId()));
  }

    public List getRecentListByOID(int objId) {
    return (recentList.get(objId) == null) ? new ArrayList() : recentList.get(objId);
  }

    public class ItemMallItem {

        public ItemMallItemTemplate template = null;
        public int count;
        public int price;
        public int order;
        public int iSale = 0;
        public int iDayWeek;
        public int iCategory2; // дополнительная категория(в пределах 0-3) никуда\выбор дня\эвент\и туда и туда
        public int iStartSale;
        public int iEndSale;
        public int iStartHour;
        public int iStartMin;
        public int iEndHour;
        public int iEndMin;
        public int iStock;
        public int iMaxStock;
        public int iWeight;
        public boolean iDropable;

        public ItemMallItem(ItemMallItemTemplate t) {
            template = t;
        }

        public boolean limit() {
            return iStock >= iMaxStock;
        }

        public boolean isLimited() {
            return iMaxStock > 0;
        }
    }

    public class ItemMallItemTemplate {

        public int brId;
        public int itemId;
        public int category;

        public ItemMallItemTemplate()
    {}
    }
}