if [ -f mysql_pass.sh ]; then
        . mysql_pass.sh
else
        . mysql_pass.sh.default
fi

mysqldump --ignore-table=${DBNAME}.game_log --ignore-table=${DBNAME}.loginserv_log --ignore-table=${DBNAME}.petitions --add-drop-table -h $DBHOST -u $USER --password=$PASS $DBNAME > l2jdb_full_backup.sql

for tab in \
       game/ally_data.sql \
       game/auction.sql \
       game/auction_bid.sql \
       game/bans.sql \
       game/bonus.sql \
       game/castle.sql \
       game/castle_manor_procure.sql \
       game/castle_manor_production.sql \
       game/character_blocklist.sql \
       game/character_bookmarks.sql \
       game/character_effects_save.sql \
       game/character_friends.sql \
       game/character_hennas.sql \
       game/character_macroses.sql \
       game/character_quests.sql \
       game/character_recipebook.sql \
       game/character_shortcuts.sql \
       game/character_skills.sql \
       game/character_skills_save.sql \
       game/character_subclasses.sql \
       game/character_variables.sql \
	   game/character_vote.sql \
       game/characters.sql \
       game/clan_data.sql \
       game/clan_notices.sql \
       game/clan_privs.sql \
       game/clan_skills.sql \
       game/clan_subpledges.sql \
       game/clan_wars.sql \
       game/clanhall.sql \
       game/couples.sql \
       game/craftcount.sql \
       game/cursed_weapons.sql \
       game/dropcount.sql \
       game/epic_boss_spawn.sql \
       game/forts.sql \
       game/game_log.sql \
       game/games.sql \
       game/global_tasks.sql \
       game/heroes.sql \
	   game/hellbound.sql \
       game/item_attributes.sql \
       game/items.sql \
       game/items_delayed.sql \
       game/killcount.sql \
       game/mail.sql \
       game/olympiad_nobles.sql \
       game/petitions.sql \
       game/pets.sql \
	   game/prime_shop.sql \
       game/raidboss_points.sql \
       game/raidboss_status.sql \
       game/residence_functions.sql \
       game/server_variables.sql \
       game/seven_signs.sql \
       game/seven_signs_festival.sql \
       game/seven_signs_status.sql \
       game/siege_clans.sql \
       game/siege_doorupgrade.sql \
       game/siege_guards.sql \
       game/siege_territory_members.sql \
       game/tournament_table.sql \
       game/tournament_teams.sql \
       game/tournament_variables.sql \
        ; do
                echo Loading $tab ...
                mysql -h $DBHOST -u $USER --password=$PASS -D $DBNAME < $tab
done
./upgrade.sh
