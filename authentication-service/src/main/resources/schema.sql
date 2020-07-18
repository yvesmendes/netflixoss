DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS user_orgs;


DROP TABLE IF EXISTS oauth_client_details;
DROP TABLE IF EXISTS oauth_client_token;
DROP TABLE IF EXISTS oauth_access_token;
DROP TABLE IF EXISTS oauth_refresh_token;
DROP TABLE IF EXISTS oauth_code;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS authorities;


CREATE TABLE user_orgs (
  organization_id   VARCHAR(100)  NOT NULL,
  user_name         VARCHAR(100)   NOT NULL,
  PRIMARY KEY (user_name));


INSERT INTO user_orgs (organization_id, user_name) VALUES ('d1859f1f-4bd7-4593-8654-ea6d9a6a626e', 'user');
INSERT INTO user_orgs (organization_id, user_name) VALUES ('42d3d4f5-9f33-42f4-8aca-b7519d6af1bb', 'william.woodward');



CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256) NOT NULL,
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4000),
  autoapprove VARCHAR(256)
);

CREATE TABLE IF NOT EXISTS oauth_client_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

-- CREATE TABLE IF NOT EXISTS oauth_access_token (
--   token_id VARCHAR(256),
--   token bytea,
--   authentication_id VARCHAR(256),
--   user_name VARCHAR(256),
--   client_id VARCHAR(256),
--   authentication bytea,
--   refresh_token VARCHAR(256)
-- );

-- CREATE TABLE IF NOT EXISTS oauth_refresh_token (
--   token_id VARCHAR(256),
--   token bytea,
--   authentication bytea
-- );

CREATE TABLE IF NOT EXISTS oauth_code (
  code VARCHAR(256), authentication bytea
);

INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity)
  VALUES ('clientId', '{bcrypt}$2a$10$vCXMWCn7fDZWOcLnIEhmK.74dvK1Eh8ae2WrWlhr2ETPLoxQctN4.', 'read,write', 'password,refresh_token,client_credentials', 'ROLE_CLIENT', 300);


CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(256) NOT NULL UNIQUE,
  password VARCHAR(256) NOT NULL
);

-- CREATE TABLE IF NOT EXISTS authorities (
--   username VARCHAR(256) NOT NULL,
--   authority VARCHAR(256) NOT NULL,
--   PRIMARY KEY(username, authority)
-- );

-- The encrypted password is `pass`
INSERT INTO users (id, username, password) VALUES (1, 'user', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC');
-- INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');

