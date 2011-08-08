@echo off
TITLE L2Phoenix Nova Edition: Установщик базы данных
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
echo.### Главное меню ###
echo.
echo.(1) Установка таблиц сервера авторизации
echo.(2) Установка таблиц игрового сервера
echo.(3) Обновление таблиц игрового сервера
echo.(4) Резервное копирование
echo.(5) Восстановление резервной копии
echo.(6) Потерянные данные
echo.(7) Установка дополнений
echo.(q) Выход
echo.
set button=x
set /p button=Что вы хотите сделать?:
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
echo.### Установка таблиц сервера авторизации ###
echo.
echo.(i) Установить. Внимание !!! Таблицы accounts, gameservers, banned_ips будут перезаписаны !!!
echo.(m) Главное меню
echo.(q) Выход
echo.
set button=x
set /p button=Ваши действия?:
if /i %button%==i goto Install_Login_Server
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_Login_Server_menu

REM ######################################## :Install_Game_Server_menu
:Install_Game_Server_menu
cls
echo.### Установка таблиц игрового сервера ###
echo.
echo.(i) Установить. Внимание !!! Всё таблицы игрового сервера будут перезаписаны !!!
echo.(m) Главное меню
echo.(q) Выход
echo.
set button=x
set /p button=Ваши действия?:
if /i %button%==i goto Install_Game_Server
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_Game_Server_menu

REM ######################################## :upgrade_menu
:upgrade_menu
cls
echo.### Обновление таблиц игрового сервера ###
echo.
echo.(u) Обновить
echo.(m) Главное меню
echo.(q) Выход
echo.
set button=x
set /p button=Ваши действия?:
if /i %button%==u goto upgrade_db
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto upgrade_menu

REM ######################################## :backup_menu
:backup_menu
cls
echo.### Меню резервного копирования ###
echo.
echo.(1) Полное резервное копирование базы данных
echo.(2) Резервное копирование только главных таблиц
echo.(m) Главное меню
echo.(q) Выход
echo.
set button=x
set /p button=Какое действие вы хотите выполнить?:
if /i %button%==1 goto full_backup
if /i %button%==2 goto general_backup
if /i %button%==m goto setup
if /i %button%==q goto end
goto backup_menu

REM ######################################## :restore_menu
:restore_menu
cls
echo.Список всех файлов в каталоге "/backup/" !
echo.
dir backup /B /P
echo.
echo.### Меню восстановления резервной копии ###
echo.
echo.Введите полное имя файла, из которого вы хотите восстановить базу данных !
echo.(m) Главное меню
echo.(q) Выход
echo.
set filename=x
set /p filename=Введите имя файла?:
if /i %filename%==m goto main_menu
if /i %filename%==q goto end
if /i %filename%==%filename% goto restore_DB
goto restore_menu

REM ######################################## :lost_data_menu
:lost_data_menu
cls
echo.### Утерянные данные ###
echo.
echo.(1) Просмотр утерянных данных
echo.(2) Удаление утерянных данных
echo.(m) Главное меню
echo.(q) Выход
echo.
set button=x
set /p button=Какое действие вы хотите выполнить?:
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
echo.Все утерянные данные удалены !!!
echo.
pause
goto lost_data_menu

REM ######################################## :install_option_data
:install_option_data
cls
echo.### Инсталляция дополнений ###
echo.
echo.(1) Установить TeleToGH SQL patch
echo.(m) Главное меню
echo.(q) Выход
echo.
set button=x
set /p button=Ваши действия?:
if /i %button%==1 goto Install_TeleToGH
if /i %button%==m goto main_menu
if /i %button%==q goto end
goto Install_TeleToGH

REM ######################################## :Install_TeleToGH
:Install_TeleToGH
echo.Установка TeleToGH SQL patch !!!
echo.
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < optional/teletogh/patch.sql
echo.
echo.TeleToGH SQL patch установленно !!!
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
echo Создание полной резервной копии в /backup/%DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% > backup/%DATE%-%ctime%_backup_full.sql
echo.
echo.Установка таблиц сервера авторизации:
echo.
echo Установка таблицы: accounts.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/accounts.sql
echo Установка таблицы: gameservers.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/gameservers.sql
echo Установка таблицы: banned_ips.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/banned_ips.sql
echo Установка таблицы: loginserv_log.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/loginserv_log.sql
echo Установка таблицы: lock.sql
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < login/lock.sql
echo.
echo.Таблицы сервера авторизации установленны !!!
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
echo Создание полной резервной копии в /backup/%DATE%-%ctime%_backup_full.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% > backup/%DATE%-%ctime%_backup_full.sql
echo.
echo.Установка основных таблиц игрового сервера:
echo.
echo Установка таблицы: ally_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/ally_data.sql
echo Установка таблицы: auction
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/auction.sql
echo Установка таблицы: auction_bid
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/auction_bid.sql
echo Установка таблицы: bans
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/bans.sql
echo Установка таблицы: bonus
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/bonus.sql
echo Установка таблицы: castle
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/castle.sql
echo Установка таблицы: castle_manor_procure
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/castle_manor_procure.sql
echo Установка таблицы: castle_manor_production
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/castle_manor_production.sql
echo Установка таблицы: character_blocklist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_blocklist.sql
echo Установка таблицы: character_bookmarks
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_bookmarks.sql
echo Установка таблицы: character_effects_save
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_effects_save.sql
echo Установка таблицы: character_friends
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_friends.sql
echo Установка таблицы: character_hennas
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_hennas.sql
echo Установка таблицы: character_macroses
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_macroses.sql
echo Установка таблицы: character_quests
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_quests.sql
echo Установка таблицы: character_recipebook
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_recipebook.sql
echo Установка таблицы: character_shortcuts
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_shortcuts.sql
echo Установка таблицы: character_skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_skills.sql
echo Установка таблицы: character_skills_save
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_skills_save.sql
echo Установка таблицы: character_subclasses
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_subclasses.sql
echo Установка таблицы: character_variables
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_variables.sql
echo Установка таблицы: character_vote
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/character_vote.sql
echo Установка таблицы: characters
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/characters.sql
echo Установка таблицы: clan_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_data.sql
echo Установка таблицы: clan_notices
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_notices.sql
echo Установка таблицы: clan_privs
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_privs.sql
echo Установка таблицы: clan_skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_skills.sql
echo Установка таблицы: clan_subpledges
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_subpledges.sql
echo Установка таблицы: clan_wars
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clan_wars.sql
echo Установка таблицы: clanhall
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/clanhall.sql
echo Установка таблицы: couples
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/couples.sql
echo Установка таблицы: craftcount
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/craftcount.sql
echo Установка таблицы: cursed_weapons
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/cursed_weapons.sql
echo Установка таблицы: dropcount
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/dropcount.sql
echo Установка таблицы: epic_boss_spawn
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/epic_boss_spawn.sql
echo Установка таблицы: forts
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/forts.sql
echo Установка таблицы: game_log
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/game_log.sql
echo Установка таблицы: games
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/games.sql
echo Установка таблицы: global_tasks
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/global_tasks.sql
echo Установка таблицы: heroes
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/heroes.sql
echo Установка таблицы: hellbound
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/hellbound.sql
echo Установка таблицы: item_attributes
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/item_attributes.sql
echo Установка таблицы: items
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/items.sql
echo Установка таблицы: items_delayed
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/items_delayed.sql
echo Установка таблицы: killcount
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/killcount.sql
echo Установка таблицы: mail
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/mail.sql
echo Установка таблицы: olympiad_nobles
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/olympiad_nobles.sql
echo Установка таблицы: petitions
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/petitions.sql
echo Установка таблицы: pets
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/pets.sql
echo Установка таблицы: prime_shop
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/prime_shop.sql
echo Установка таблицы: raidboss_points
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/raidboss_points.sql
echo Установка таблицы: raidboss_status
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/raidboss_status.sql
echo Установка таблицы: residence_functions
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/residence_functions.sql
echo Установка таблицы: server_variables
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/server_variables.sql
echo Установка таблицы: seven_signs
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/seven_signs.sql
echo Установка таблицы: seven_signs_festival
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/seven_signs_festival.sql
echo Установка таблицы: seven_signs_status
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/seven_signs_status.sql
echo Установка таблицы: siege_clans
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_clans.sql
echo Установка таблицы: siege_doorupgrade
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_doorupgrade.sql
echo Установка таблицы: siege_guards
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_guards.sql
echo Установка таблицы: siege_territory_members
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/siege_territory_members.sql
echo Установка таблицы: tournament_table
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/tournament_table.sql
echo Установка таблицы: tournament_teams
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/tournament_teams.sql
echo Установка таблицы: tournament_variables
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < game/tournament_variables.sql

goto upgrade_db

REM ######################################## :upgrade_db
:upgrade_db
echo.
echo.Установка таблиц обновлений !!!
echo.
echo Установка таблицы: ai_params
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/ai_params.sql
echo Установка таблицы: armor
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/armor.sql
echo Установка таблицы: armor_ex
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/armor_ex.sql
echo Установка таблицы: armorsets
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/armorsets.sql
echo Установка таблицы: auto_chat
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/auto_chat.sql
echo Установка таблицы: auto_chat_text
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/auto_chat_text.sql
echo Установка таблицы: char_templates
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/char_templates.sql
echo Установка таблицы: class_list
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/class_list.sql
echo Установка таблицы: droplist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/droplist.sql
echo Установка таблицы: etcitem
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/etcitem.sql
echo Установка таблицы: fish
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/fish.sql
echo Установка таблицы: fishreward
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/fishreward.sql
echo Установка таблицы: four_sepulchers_spawnlist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/four_sepulchers_spawnlist.sql
echo Установка таблицы: henna
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/henna.sql
echo Установка таблицы: henna_trees
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/henna_trees.sql
echo Установка таблицы: lastimperialtomb_spawnlist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/lastimperialtomb_spawnlist.sql
echo Установка таблицы: locations
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/locations.sql
echo Установка таблицы: lvlupgain
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/lvlupgain.sql
echo Установка таблицы: mapregion
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/mapregion.sql
echo Установка таблицы: merchant_areas_list
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/merchant_areas_list.sql
echo Установка таблицы: minions
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/minions.sql
echo Установка таблицы: npc
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/npc.sql
echo Установка таблицы: npcskills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/npcskills.sql
echo Установка таблицы: pet_data
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/pet_data.sql
echo Установка таблицы: pets_skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/pets_skills.sql
echo Установка таблицы: random_spawn
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/random_spawn.sql
echo Установка таблицы: random_spawn_loc
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/random_spawn_loc.sql
echo Установка таблицы: recipes
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/recipes.sql
echo Установка таблицы: recitems
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/recitems.sql
echo Установка таблицы: siege_door
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/siege_door.sql
echo Установка таблицы: skills
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skills.sql
echo Установка таблицы: skill_learn
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skill_learn.sql
echo Установка таблицы: skill_spellbooks
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skill_spellbooks.sql
echo Установка таблицы: skill_trees
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/skill_trees.sql
echo Установка таблицы: spawnlist
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/spawnlist.sql
echo Установка таблицы: tournament_class_list
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/tournament_class_list.sql
echo Установка таблицы: weapon
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/weapon.sql
echo Установка таблицы: weapon_ex
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/weapon_ex.sql
echo Установка таблицы: updates
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < upgrade/updates.sql
echo.
echo.Таблицы игрового сервера инсталлированы !!!
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
echo Создание полной резервной копии в /backup/%DATE%-%ctime%_backup_full.sql
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
echo Создание резервной копии главных таблиц в /backup/%DATE%-%ctime%_backup_general.sql
echo.
mysqldump.exe %Ignore% --add-drop-table -h %DBHost% -u %user% --password=%pass% %DBname% %Generaltables% > backup/%DATE%-%ctime%_backup_general.sql
goto end

REM ######################################## :restore_DB
:restore_DB
if not exist backup/%filename% (
echo.
echo.Файл не найден !
echo.
pause
goto restore_menu
)
cls
echo.
echo.Восстановление вайла /backup/%filename% !
echo.
pause
mysql.exe -h %DBHost% -u %user% --password=%pass% -D %DBname% < backup/%filename%
goto end

REM ######################################## :not_working_now
:not_working_now
echo.
echo Не работает сейчас !!!
echo.
pause
goto main_menu

REM ######################################## :end
:end
