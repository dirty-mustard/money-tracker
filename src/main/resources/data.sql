-- User
INSERT INTO MT_TB_USERS (ID, USERNAME, PASSWORD)
    VALUES (
    NEXTVAL('MT_SEQ_USERS'),
    'admin',
    '$2a$10$uzM0DZbQrAVZZnsyf2YgMeCQw0IyBlT1gg.lqRj34.mNGDIQ6AyW2' -- password
    );

-- Expenses
INSERT INTO MT_TB_TAGS (ID, OWNER_ID, NAME, COLOR)
    VALUES (NEXTVAL('MT_SEQ_TAGS'), CURRVAL('MT_SEQ_USERS'), 'Expenses', '#ff0000');

INSERT INTO MT_TB_FILTERS (ID, OWNER_ID, NAME, AMOUNT_TO)
    VALUES (NEXTVAL('MT_SEQ_FILTERS'), CURRVAL('MT_SEQ_USERS'), 'Filter name #1', 0);
INSERT INTO MT_TB_FILTER_OPTIONS (FILTER_ID, NAME)
    VALUES (CURRVAL('MT_SEQ_FILTERS'), 'UNTAGGED');

INSERT INTO MT_TB_RULES (ID, OWNER_ID, FILTER_ID)
    VALUES (NEXTVAL('MT_SEQ_RULES'), CURRVAL('MT_SEQ_USERS'), CURRVAL('MT_SEQ_FILTERS'));
INSERT INTO MT_TB_RULE_TAGS (RULE_ID, TAG_ID)
    VALUES (CURRVAL('MT_SEQ_RULES'), (SELECT ID FROM MT_TB_TAGS WHERE NAME = 'Expenses'));

-- Income
INSERT INTO MT_TB_TAGS (ID, OWNER_ID, NAME, COLOR)
    VALUES (NEXTVAL('MT_SEQ_TAGS'), CURRVAL('MT_SEQ_USERS'), 'Income', '#00ff00');

INSERT INTO MT_TB_FILTERS (ID, OWNER_ID, NAME, AMOUNT_FROM)
    VALUES (NEXTVAL('MT_SEQ_FILTERS'), CURRVAL('MT_SEQ_USERS'), 'Filter name #2', 0);
INSERT INTO MT_TB_FILTER_OPTIONS (FILTER_ID, NAME)
    VALUES (CURRVAL('MT_SEQ_FILTERS'), 'UNTAGGED');

INSERT INTO MT_TB_RULES (ID, OWNER_ID, FILTER_ID)
    VALUES (NEXTVAL('MT_SEQ_RULES'), CURRVAL('MT_SEQ_USERS'), CURRVAL('MT_SEQ_FILTERS'));
INSERT INTO MT_TB_RULE_TAGS (RULE_ID, TAG_ID)
    VALUES (CURRVAL('MT_SEQ_RULES'), (SELECT ID FROM MT_TB_TAGS WHERE NAME = 'Income'));

-- Supermarket
INSERT INTO MT_TB_TAGS (ID, OWNER_ID, NAME, COLOR)
    VALUES (NEXTVAL('MT_SEQ_TAGS'), CURRVAL('MT_SEQ_USERS'), 'Supermarket', '#0000ff');

INSERT INTO MT_TB_FILTERS (ID, OWNER_ID, NAME, DESCRIPTION)
    VALUES (NEXTVAL('MT_SEQ_FILTERS'), CURRVAL('MT_SEQ_USERS'), 'SUMA', 'SUMA');
INSERT INTO MT_TB_FILTER_OPTIONS (FILTER_ID, NAME)
    VALUES (CURRVAL('MT_SEQ_FILTERS'), 'UNTAGGED');

INSERT INTO MT_TB_RULES (ID, OWNER_ID, FILTER_ID)
    VALUES (NEXTVAL('MT_SEQ_RULES'), CURRVAL('MT_SEQ_USERS'), CURRVAL('MT_SEQ_FILTERS'));
INSERT INTO MT_TB_RULE_TAGS (RULE_ID, TAG_ID)
    VALUES (CURRVAL('MT_SEQ_RULES'), (SELECT ID FROM MT_TB_TAGS WHERE NAME = 'Supermarket'));

-- Reports
INSERT INTO MT_TB_FILTERS (ID, OWNER_ID, NAME)
    VALUES (NEXTVAL('MT_SEQ_FILTERS'), CURRVAL('MT_SEQ_USERS'), 'Supermarket');
INSERT INTO MT_TB_FILTER_TAGS (FILTER_ID, TAG_ID)
    VALUES (CURRVAL('MT_SEQ_FILTERS'), (SELECT ID FROM MT_TB_TAGS WHERE NAME = 'Supermarket'));

INSERT INTO MT_TB_REPORTS (ID, OWNER_ID, NAME, ICON, FILTER_ID)
    VALUES (NEXTVAL('MT_SEQ_REPORTS'), CURRVAL('MT_SEQ_USERS'), 'Supermarket', 'fa-shopping-cart', CURRVAL('MT_SEQ_FILTERS'));
INSERT INTO MT_TB_REPORT_CHARTS (REPORT_ID, NAME)
    VALUES (CURRVAL('MT_SEQ_REPORTS'), 'PIE');
INSERT INTO MT_TB_REPORT_CHARTS (REPORT_ID, NAME)
    VALUES (CURRVAL('MT_SEQ_REPORTS'), 'LINE');
