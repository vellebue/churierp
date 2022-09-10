--Users Model

--User login angel passwd angel8
select nextval('seq_users');
INSERT INTO users (user_id, creation_user, creation_time, update_user, update_time, login,"password","name",surname,email,creation_date,end_date) VALUES
	 (0, 'root', CURRENT_TIMESTAMP, 'root', CURRENT_TIMESTAMP, 'angel','{YjFGd5N2gfpbcucFL2J3uJjfIp8BgT+oMBl1i7wv6kM=}9fbf567e3dcc32e6905c465fbc63f9ccc57fe305d14f7c9a55458126baba1803',
	  'Ángel','García Bastanchuri','vellebue@gmail.com','2022-07-17',NULL);

--Addresses model
INSERT INTO c_address_types (type_id,description,"key") VALUES
	 ('COMP', 'Company Address', 'churierpweb.address.type.comp');

commit;


