-- Sample data for local/dev usage.
-- Requires profile 'dev' (see application-dev.properties).

INSERT INTO library (
    id_library,
    name,
    type,
    supports_completion,
    is_gradeable,
    is_thematic,
    supports_wishlist,
    tracks_dates,
    supports_price,
    description,
    genre_layout_mode,
    is_compact,
    position_index,
    supports_progress,
    progress_type,
    custom_progress_unit,
    default_category,
    rating_scale
) VALUES (
    1,
    'Mi biblioteca',
    'BOOKS',
    true,
    true,
    false,
    true,
    true,
    true,
    'Biblioteca de ejemplo para desarrollo.',
    0,
    false,
    0,
    true,
    'PAGES',
    null,
    'General',
    10
);

INSERT INTO item (
    id_item,
    id_library,
    name,
    description,
    status,
    is_wishlist,
    is_current,
    is_collection
) VALUES
(
    100,
    1,
    'El nombre del viento',
    'Ejemplo de item con progreso.',
    'IN_PROGRESS',
    false,
    true,
    false
),
(
    101,
    1,
    'Clean Code',
    'Ejemplo de item en wishlist.',
    'PENDING',
    true,
    false,
    false
);

INSERT INTO item_image (
    id_image,
    id_item,
    image_uri,
    remote_image_url
) VALUES (
    1000,
    100,
    'covers/el-nombre-del-viento.jpg',
    'https://example.invalid/covers/el-nombre-del-viento.jpg'
);

INSERT INTO attribute_type (
    attribute_type_id,
    name,
    data_type
) VALUES
(
    10,
    'Autor',
    'STRING'
),
(
    11,
    'ISBN',
    'STRING'
);

INSERT INTO attribute_item (
    attribute_item_id,
    id_item,
    attribute_type_id,
    value
) VALUES
(
    2000,
    100,
    10,
    'Patrick Rothfuss'
),
(
    2001,
    101,
    11,
    '9780132350884'
);

