
docker-compose run --rm --entrypoint "\
certbot certonly \
-d main-gj.com \
-d api.main-gj.com \
-d admin.main-gj.com \
--email ttea@gmail.com \
--manual --preferred-challenges dns \
--server https://acme-v02.api.letsencrypt.org/directory \
--force-renewal" certbot

docker-compose exec nginx_server nginx -s reload