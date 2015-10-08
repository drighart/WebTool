----------------------------------------------------------------------------------------------------
--- INITIAL SETUP DATABASE
----------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS DATA.PLUGIN(CREATED TIMESTAMP, MODIFIED TIMESTAMP, PLUGIN_ID VARCHAR(64), VERSION INTEGER, RUNNING BOOLEAN);

--- USERS -----------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DATA.USER(CREATED TIMESTAMP, MODIFIED TIMESTAMP, EMAIL_ADDRESS VARCHAR(64) PRIMARY KEY, CREDENTIALS VARCHAR(64), 
    ROLES VARCHAR(64), DISPLAY_NAME VARCHAR(64), ACTIVATED BOOLEAN, LANGUAGE VARCHAR(10));
-- INSERT INTO DATA.USER VALUES (NOW(), NOW(), 'guest@example.com', 'guest', 'user', 'Guest', true, 'en_US');
-- INSERT INTO DATA.USER VALUES (NOW(), NOW(), 'admin@example.com', 'admin', 'admin', 'Admin', true, 'en_US');
-- INSERT INTO DATA.USER VALUES (NOW(), NOW(), 'editor@example.com', 'editor', 'editor', 'Editor', true, 'en_US');

--- CONFIGURATION ITEMS ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DATA.CONFIGURATION_ITEM(CREATED TIMESTAMP, MODIFIED TIMESTAMP, NAME VARCHAR(64) PRIMARY KEY, VALUE VARCHAR(255), PLUGIN_ID VARCHAR(255), TYPE_OF_INPUT VARCHAR(25));

INSERT INTO DATA.CONFIGURATION_ITEM VALUES (NOW(), NOW(), 'Website name', 'DRDevelopment 1.0', '', '');
INSERT INTO DATA.CONFIGURATION_ITEM VALUES (NOW(), NOW(), 'Website title', 'THIS IS THE TITLE OF YOUR NEW WEBSITE!', '', '');
INSERT INTO DATA.CONFIGURATION_ITEM VALUES (NOW(), NOW(), 'Website sub-title', 
    'This is the sub-title of your website. With WebTool you can make the most amazing websites in 5 minutes. Try it yourself!', '', '');
INSERT INTO DATA.CONFIGURATION_ITEM VALUES (NOW(), NOW(), 'Contact Phone', '123-456-6789', '', '');
INSERT INTO DATA.CONFIGURATION_ITEM VALUES (NOW(), NOW(), 'Contact Email', 'feedback@startbootstrap.com', '', '');
    
--- PAGES -----------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DATA.PAGE(ID INTEGER PRIMARY KEY AUTO_INCREMENT, CREATED TIMESTAMP, MODIFIED TIMESTAMP, NAME VARCHAR(64), 
	TITLE VARCHAR(64), POSITION INTEGER);

-- INSERT INTO DATA.PAGE VALUES (1, NOW(), NOW(), 'WELCOME', 'Title welcome page', 1);

--- PARAGRAPH -------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DATA.PARAGRAPH(ID INTEGER PRIMARY KEY AUTO_INCREMENT, CREATED TIMESTAMP, MODIFIED TIMESTAMP,
	POSITION INTEGER, TEMPLATE VARCHAR(64), CONTENT CLOB, BUTTON_TEXT VARCHAR(64), BUTTON_LINK VARCHAR(64),
	PAGE_ID INTEGER, ANCHOR VARCHAR(64), IMAGE_NAME VARCHAR(64), IMAGE_ALIGNMENT VARCHAR(20), BACKGROUND_COLOR VARCHAR(40), 
	BACKGROUND_IMAGE VARCHAR(64));

-- INSERT INTO DATA.PARAGRAPH VALUES (1, NOW(), NOW(), 1, 'TEXT', 'This is the introduction of the page.', '', 1);
-- INSERT INTO DATA.PARAGRAPH VALUES (2, NOW(), NOW(), 2, 'TEXT', 'This is the real text of the page.', '', 1);

--- MENUS -----------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DATA.MENU_ITEM(CREATED TIMESTAMP, MODIFIED TIMESTAMP, POSITION INTEGER, NAME VARCHAR(64), PAGE_ID INTEGER, ON_CURRENT_PAGE BOOLEAN);

INSERT INTO DATA.MENU_ITEM VALUES (NOW(), NOW(), 1, 'Welcome', 1, true);
INSERT INTO DATA.MENU_ITEM VALUES (NOW(), NOW(), 2, 'About', 2, true);
INSERT INTO DATA.MENU_ITEM VALUES (NOW(), NOW(), 3, 'Services', 3, true);
INSERT INTO DATA.MENU_ITEM VALUES (NOW(), NOW(), 4, 'Contact', 4, true);

--- IMAGES ----------------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DATA.IMAGE(CREATED TIMESTAMP, MODIFIED TIMESTAMP, NAME VARCHAR(64) PRIMARY KEY, TAGS VARCHAR(64));
