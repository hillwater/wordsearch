CREATE USER wordsearch with password 'wordsearch';
CREATE DATABASE wordsearch OWNER wordsearch ENCODING 'UTF8' LC_COLLATE 'en_US.utf8' LC_CTYPE 'en_US.utf8' TEMPLATE template0;

GRANT ALL ON ALL TABLES IN SCHEMA public TO wordsearch;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO wordsearch;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO wordsearch;
