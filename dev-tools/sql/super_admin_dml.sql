INSERT INTO users (
    email,
    password,
    first_name,
    middle_name,
    last_name,
    phone_number,
    address,
    role,
    active,
    created_at,
    created_by,
    modified_at,
    modified_by
) VALUES (
    'superadmin@ims.local',
    '$2a$10$Jif05h9lLkD444RYBZcTSeFd04fUDCRi7mserVqRWYK.f/AGPIvtK',
    'Super',
    NULL,
    'Admin',
    '0000000000',
    'System default user',
    'SUPER_ADMIN',
    true,
    NOW(),
    'SYSTEM',
    NOW(),
    'SYSTEM'
);