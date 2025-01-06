
-- insert into users(username, password, enabled ) values('card','$2y$12$/nkxGM78hPXrvROH1AYJiOsJQeUm0Rq8gSKZ99ZTNAVhBI48.79WW',TRUE);


drop table if exists oauth_client_details;

create table oauth_client_details (
       client_id VARCHAR(255) PRIMARY KEY,
       resource_ids VARCHAR(255),
       client_secret VARCHAR(255),
       scope VARCHAR(255),
       authorized_grant_types VARCHAR(255),
       web_server_redirect_uri VARCHAR(255),
       authorities VARCHAR(255),
       access_token_validity INTEGER,
       refresh_token_validity INTEGER,
       additional_information VARCHAR(4096),
       autoapprove VARCHAR(255)
);
