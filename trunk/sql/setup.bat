@echo off
TITLE L2Phoenix Nova Edition: ��⠭��騪 ���� ������
REM ######################################## Automatic updater for L2Phoenix Nova Edition - Do not edit !!!
goto answer%ERRORLEVEL%
:answerTrue
set fastend=yes
goto upgrade_db
:answer0
set fastend=no

set user=root
set pass=root
set DBname=lin2srv_db
set DBHost=localhost

set Generaltables=accounts augmentations clanhall gameservers banned_ips loginserv_log character_friends character_hennas character_macroses character_quests character_recipebook character_shortcuts character_skills character_effects_save character_skills_save character_subclasses characters character_variables clanhall_bids clanhall_data clan_data clanhall_decorations_bids ally_data clan_wars items pets server_variables seven_signs seven_signs_festival siege_clans killcount dropcount craftcount game_log petitions seven_signs_status global_tasks raidboss_status manor_crop manor_seeds
set Ignore=--ignore-table=%DBname%.game_log --ignore-table=%DBname%.loginserv_log --ignore-table=%DBname%.petitions

REM ########################################
mysql.exe -h %DBHost% -u %user% --password=%pass% --execute="CREATE DATABASE IF NOT EXISTS %DBname%"
if not exist backup (
mkdir backup
)

REM ######################################## :main_menu
:main_menu
cls
echo.### ������� ���� ###
echo.
echo.(1) ��⠭���� ⠡��� �ࢥ� ���ਧ�樨
echo.(2) ��⠭���� ⠡��� ��஢��� �ࢥ�
echo.(3) ���������� ⠡��� ��஢��� �ࢥ�
echo.(4) ����ࢭ�� ����஢����
echo.(5) ����⠭������� १�ࢭ�� �����
echo.(6) ����ﭭ� �����
echo.(7) ��⠭���� ����������
echo.(q) ��室
echo.
set button=x
set /p button=�� �� ��� ᤥ����?:
if /i %button%==1 goto Install_Login_Server_menu
if /i %button%==2 goto Install_Game_Server_menu
if /i %button%==3 goto upgrade_menu
if /i %button%==4 goto backup_menu
if /i %button%==5 goto restore_menu
if /i %button%==6 goto lost_data_menu
if /i %button%==7 goto install_option_data
if /i %button%==q goto end
goto main_menu

REM ######################################## :Install_Login_Server_menu
:Install_Login_Server_menu
cls
echo.### ��⠭���� ⠡��� �ࢥ� ���ਧ�樨 ###
echo.
echo.(i) ��⠭�����. �������� !!! ������� accounts, gameservers, banned_ips ���� ��१���ᠭ� !!!
echo.(m) ������� ����
echo.(q) ��室
echo.
set button=x
set /p button=��� ����⢨�?:
if /i %button%==i goto Install_Login_Server
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_Login_Server_menu

REM ######################################## :Install_Game_Server_menu
:Install_Game_Server_menu
cls
echo.### ��⠭���� ⠡��� ��஢��� �ࢥ� ###
echo.
echo.(i) ��⠭�����. �������� !!! ��� ⠡���� ��஢��� �ࢥ� ���� ��१���ᠭ� !!!
echo.(m) ������� ����
echo.(q) ��室
echo.
set button=x
set /p button=��� ����⢨�?:
if /i %button%==i goto Install_Game_Server
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_Game_Server_menu

REM ######################################## :upgrade_menu
:upgrade_menu
cls
echo.### ���������� ⠡��� ��஢��� �ࢥ� ###
echo.
echo.(u) ��������
echo.(m) ������� ����
echo.(q) ��室
echo.
set button=x
set /p button=��� ����⢨�?:
if /i %button%==u goto upgrade_db
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto upgrade_menu

REM ######################################## :backup_menu
:backup_menu
cls
echo.### ���� १�ࢭ��� ����஢���� ###
echo.
echo.(1) ������ १�ࢭ�� ����஢���� ���� ������
echo.(2) ����ࢭ�� ����஢���� ⮫쪮 ������� ⠡���
echo.(m) ������� ����
echo.(q) ��室
echo.
set button=x
set /p button=����� ����⢨� �� ��� �믮�����?:
if /i %button%==1 goto full_backup
if /i %button%==2 goto general_backup
if /i %button%==m goto setup
if /i %button%==q goto end
goto backup_menu

REM ######################################## :restore_menu
:restore_menu
cls
echo.���᮪ ��� 䠩��� � ��⠫��� "/backup/" !
echo.
dir backup /B /P
echo.
echo.### ���� ����⠭������� १�ࢭ�� ����� ###
echo.
echo.������ ������ ��� 䠩��, �� ���ண� �� ��� ����⠭����� ���� ������ !
echo.(m) ������� ����
echo.(q) ��室
echo.
set filename=x
set /p filename=������ ��� 䠩��?:
if /i %filename%==m goto main_menu
if /i %filename%==q goto end
if /i %filename%==%filename% goto restore_DB
goto restore_menu

REM ######################################## :lost_data_menu
:lost_data_menu
cls
echo.### ���ﭭ� ����� ###
echo.
echo.(1) ��ᬮ�� ���ﭭ�� ������
echo.(2) �������� ���ﭭ�� ������
echo.(m) ������� ����
echo.(q) ��室
echo.
set button=x
set /p button=����� ����⢨� �� ��� �믮�����?:
if /i %button%==1 goto show_lost_data
if /i %button%==2 goto delete_lost_data
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto lost_data_menu

:show_lost_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < tools/maintenance/lost_data_show.sql
pause
goto lost_data_menu

:delete_lost_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < tools/maintenance/lost_data_del.sql
echo.
echo.�� ���ﭭ� ����� 㤠���� !!!
echo.
pause
goto lost_data_menu

REM ######################################## :install_option_data
:install_option_data
cls
echo.### ���⠫���� ���������� ###
echo.
echo.(1) ��⠭����� TeleToGH SQL patch
echo.(m) ������� ����
echo.(q) ��室
echo.
set button=x
set /p button=��� ����⢨�?:
if /i %button%==1 goto Install_TeleToGH
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_TeleToGH

REM ######################################## :Install_TeleToGH
:Install_TeleToGH
echo.��⠭���� TeleToGH SQL patch !!!
echo.
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < optional/teletogh/patch.sql
echo.
echo.TeleToGH SQL patch ��⠭������� !!!
echo.
pause
goto main_menu

REM ######################################## :Install_Login_Server
:Install_Login_Server
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%'%TIME:~3,2%'%TIME:~6,2%
echo.
echo �������� ������ १�ࢭ�� ����� � /backup/%DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% > backup/%DATE%-%ctime%_backup_full.sql
echo.
echo.��⠭���� ⠡��� �ࢥ� ���ਧ�樨:
echo.
echo ��⠭���� ⠡����: accounts.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/accounts.sql
echo ��⠭���� ⠡����: gameservers.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/gameservers.sql
echo ��⠭���� ⠡����: banned_ips.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/banned_ips.sql
echo ��⠭���� ⠡����: loginserv_log.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/loginserv_log.sql
echo ��⠭���� ⠡����: lock.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/lock.sql
echo.
echo.������� �ࢥ� ���ਧ�樨 ��⠭������� !!!
echo.
pause
goto main_menu

REM ######################################## :Install_Game_Server
:Install_Game_Server
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%'%TIME:~3,2%'%TIME:~6,2%
echo.
echo �������� ������ १�ࢭ�� ����� � /backup/%DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% > backup/%DATE%-%ctime%_backup_full.sql
echo.
echo.��⠭���� �᭮���� ⠡��� ��஢��� �ࢥ�:
echo.
echo ��⠭���� ⠡����: ally_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/ally_data.sql
echo ��⠭���� ⠡����: auction
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/auction.sql
echo ��⠭���� ⠡����: auction_bid
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/auction_bid.sql
echo ��⠭���� ⠡����: bans
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/bans.sql
echo ��⠭���� ⠡����: bonus
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/bonus.sql
echo ��⠭���� ⠡����: castle
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/castle.sql
echo ��⠭���� ⠡����: castle_manor_procure
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/castle_manor_procure.sql
echo ��⠭���� ⠡����: castle_manor_production
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/castle_manor_production.sql
echo ��⠭���� ⠡����: character_blocklist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_blocklist.sql
echo ��⠭���� ⠡����: character_bookmarks
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_bookmarks.sql
echo ��⠭���� ⠡����: character_effects_save
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_effects_save.sql
echo ��⠭���� ⠡����: character_friends
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_friends.sql
echo ��⠭���� ⠡����: character_hennas
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_hennas.sql
echo ��⠭���� ⠡����: character_macroses
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_macroses.sql
echo ��⠭���� ⠡����: character_quests
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_quests.sql
echo ��⠭���� ⠡����: character_recipebook
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_recipebook.sql
echo ��⠭���� ⠡����: character_shortcuts
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_shortcuts.sql
echo ��⠭���� ⠡����: character_skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_skills.sql
echo ��⠭���� ⠡����: character_skills_save
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_skills_save.sql
echo ��⠭���� ⠡����: character_subclasses
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_subclasses.sql
echo ��⠭���� ⠡����: character_variables
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_variables.sql
echo ��⠭���� ⠡����: character_vote
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_vote.sql
echo ��⠭���� ⠡����: characters
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/characters.sql
echo ��⠭���� ⠡����: clan_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_data.sql
echo ��⠭���� ⠡����: clan_notices
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_notices.sql
echo ��⠭���� ⠡����: clan_privs
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_privs.sql
echo ��⠭���� ⠡����: clan_skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_skills.sql
echo ��⠭���� ⠡����: clan_subpledges
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_subpledges.sql
echo ��⠭���� ⠡����: clan_wars
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_wars.sql
echo ��⠭���� ⠡����: clanhall
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clanhall.sql
echo ��⠭���� ⠡����: couples
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/couples.sql
echo ��⠭���� ⠡����: craftcount
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/craftcount.sql
echo ��⠭���� ⠡����: cursed_weapons
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/cursed_weapons.sql
echo ��⠭���� ⠡����: dropcount
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/dropcount.sql
echo ��⠭���� ⠡����: epic_boss_spawn
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/epic_boss_spawn.sql
echo ��⠭���� ⠡����: forts
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/forts.sql
echo ��⠭���� ⠡����: game_log
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/game_log.sql
echo ��⠭���� ⠡����: games
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/games.sql
echo ��⠭���� ⠡����: global_tasks
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/global_tasks.sql
echo ��⠭���� ⠡����: heroes
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/heroes.sql
echo ��⠭���� ⠡����: hellbound
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/hellbound.sql
echo ��⠭���� ⠡����: item_attributes
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/item_attributes.sql
echo ��⠭���� ⠡����: items
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/items.sql
echo ��⠭���� ⠡����: items_delayed
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/items_delayed.sql
echo ��⠭���� ⠡����: killcount
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/killcount.sql
echo ��⠭���� ⠡����: mail
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/mail.sql
echo ��⠭���� ⠡����: olympiad_nobles
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/olympiad_nobles.sql
echo ��⠭���� ⠡����: petitions
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/petitions.sql
echo ��⠭���� ⠡����: pets
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/pets.sql
echo ��⠭���� ⠡����: prime_shop
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/prime_shop.sql
echo ��⠭���� ⠡����: raidboss_points
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/raidboss_points.sql
echo ��⠭���� ⠡����: raidboss_status
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/raidboss_status.sql
echo ��⠭���� ⠡����: residence_functions
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/residence_functions.sql
echo ��⠭���� ⠡����: server_variables
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/server_variables.sql
echo ��⠭���� ⠡����: seven_signs
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/seven_signs.sql
echo ��⠭���� ⠡����: seven_signs_festival
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/seven_signs_festival.sql
echo ��⠭���� ⠡����: seven_signs_status
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/seven_signs_status.sql
echo ��⠭���� ⠡����: siege_clans
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_clans.sql
echo ��⠭���� ⠡����: siege_doorupgrade
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_doorupgrade.sql
echo ��⠭���� ⠡����: siege_guards
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_guards.sql
echo ��⠭���� ⠡����: siege_territory_members
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_territory_members.sql
echo ��⠭���� ⠡����: tournament_table
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/tournament_table.sql
echo ��⠭���� ⠡����: tournament_teams
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/tournament_teams.sql
echo ��⠭���� ⠡����: tournament_variables
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/tournament_variables.sql

goto upgrade_db

REM ######################################## :upgrade_db
:upgrade_db
echo.
echo.��⠭���� ⠡��� ���������� !!!
echo.
echo ��⠭���� ⠡����: ai_params
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/ai_params.sql
echo ��⠭���� ⠡����: armor
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/armor.sql
echo ��⠭���� ⠡����: armor_ex
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/armor_ex.sql
echo ��⠭���� ⠡����: armorsets
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/armorsets.sql
echo ��⠭���� ⠡����: auto_chat
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/auto_chat.sql
echo ��⠭���� ⠡����: auto_chat_text
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/auto_chat_text.sql
echo ��⠭���� ⠡����: char_templates
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/char_templates.sql
echo ��⠭���� ⠡����: class_list
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/class_list.sql
echo ��⠭���� ⠡����: droplist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/droplist.sql
echo ��⠭���� ⠡����: etcitem
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/etcitem.sql
echo ��⠭���� ⠡����: fish
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/fish.sql
echo ��⠭���� ⠡����: fishreward
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/fishreward.sql
echo ��⠭���� ⠡����: four_sepulchers_spawnlist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/four_sepulchers_spawnlist.sql
echo ��⠭���� ⠡����: henna
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/henna.sql
echo ��⠭���� ⠡����: henna_trees
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/henna_trees.sql
echo ��⠭���� ⠡����: lastimperialtomb_spawnlist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/lastimperialtomb_spawnlist.sql
echo ��⠭���� ⠡����: locations
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/locations.sql
echo ��⠭���� ⠡����: lvlupgain
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/lvlupgain.sql
echo ��⠭���� ⠡����: mapregion
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/mapregion.sql
echo ��⠭���� ⠡����: merchant_areas_list
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/merchant_areas_list.sql
echo ��⠭���� ⠡����: minions
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/minions.sql
echo ��⠭���� ⠡����: npc
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/npc.sql
echo ��⠭���� ⠡����: npcskills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/npcskills.sql
echo ��⠭���� ⠡����: pet_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/pet_data.sql
echo ��⠭���� ⠡����: pets_skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/pets_skills.sql
echo ��⠭���� ⠡����: random_spawn
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/random_spawn.sql
echo ��⠭���� ⠡����: random_spawn_loc
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/random_spawn_loc.sql
echo ��⠭���� ⠡����: recipes
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/recipes.sql
echo ��⠭���� ⠡����: recitems
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/recitems.sql
echo ��⠭���� ⠡����: siege_door
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/siege_door.sql
echo ��⠭���� ⠡����: skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skills.sql
echo ��⠭���� ⠡����: skill_learn
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skill_learn.sql
echo ��⠭���� ⠡����: skill_spellbooks
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skill_spellbooks.sql
echo ��⠭���� ⠡����: skill_trees
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skill_trees.sql
echo ��⠭���� ⠡����: spawnlist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/spawnlist.sql
echo ��⠭���� ⠡����: tournament_class_list
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/tournament_class_list.sql
echo ��⠭���� ⠡����: weapon
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/weapon.sql
echo ��⠭���� ⠡����: weapon_ex
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/weapon_ex.sql
echo ��⠭���� ⠡����: updates
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/updates.sql
echo.
echo.������� ��஢��� �ࢥ� ���⠫��஢��� !!!
echo.
if /I %fastend%==yes goto :EOF
pause
goto main_menu

REM ######################################## :full_backup
:full_backup
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%'%TIME:~3,2%'%TIME:~6,2%
echo.
echo �������� ������ १�ࢭ�� ����� � /backup/%DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% > backup/%DATE%-%ctime%_backup_full.sql
goto end

REM ######################################## :general_backup
:general_backup
set ctime=%TIME:~0,2%
if "%ctime:~0,1%" == " " (
set ctime=0%ctime:~1,1%
)
set ctime=%ctime%'%TIME:~3,2%'%TIME:~6,2%
echo.
echo �������� १�ࢭ�� ����� ������� ⠡��� � /backup/%DATE%-%ctime%_backup_general.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% %Generaltables% > backup/%DATE%-%ctime%_backup_general.sql
goto end

REM ######################################## :restore_DB
:restore_DB
if not exist backup/%filename% (
echo.
echo.���� �� ������ !
echo.
pause
goto restore_menu
)
cls
echo.
echo.����⠭������� ����� /backup/%filename% !
echo.
pause
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < backup/%filename%
goto end

REM ######################################## :not_working_now
:not_working_now
echo.
echo �� ࠡ�⠥� ᥩ�� !!!
echo.
pause
goto main_menu

REM ######################################## :end
:end
