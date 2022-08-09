insert into users (id, email, password)
values (nextval('users_sequence'), 'admin@admin.com', '$2a$10$3TLjSfivla69omi8M0bAGeu0QNpaSe/G5Y.IMOnasUlqYw9yrnuwC');

insert into roles (id, name)
values
    (nextval('roles_sequence'), 'ADMIN'),
    (nextval('roles_sequence'), 'USER');

insert into user_roles (user_id, role_id)
values
    (1, 1);