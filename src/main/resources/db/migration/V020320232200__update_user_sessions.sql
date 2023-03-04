ALTER TABLE public.user_sessions ADD permit_extension bool NOT NULL DEFAULT false;
ALTER TABLE public.user_sessions ALTER COLUMN permit_extension DROP DEFAULT