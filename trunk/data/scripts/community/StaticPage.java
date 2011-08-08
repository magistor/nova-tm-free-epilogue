package community;

/**
 * User: Shaitan
 * Date: 29.12.10
 * Time: 18:44
 */
public class StaticPage
{
	private static String pageTemplate;

	public static void setPageTemplate(String s)
	{
		pageTemplate = s;
	}

	public static String getPageTemplate()
	{
		return pageTemplate;
	}

	public static String pageMain;
	public static String pageShop;
	public static String pageBuffer;
	public static String pageService;
	public static String pageCareer;
	public static String pageTeleport;
	public static String pageStatistic;
	public static String pageEvents;
	public static String pageStatisticPvP;
	public static String pageStatisticPK;
}