CREATE TABLE IF NOT EXISTS public.order_products
(
    order_id integer NOT NULL,
    product_id integer NOT NULL,
    CONSTRAINT fkawxpt1ns1sr7al76nvjkv21of FOREIGN KEY (order_id)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkdxjduvg7991r4qja26fsckxv8 FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

ALTER TABLE IF EXISTS public.order_products
    OWNER to postgres;