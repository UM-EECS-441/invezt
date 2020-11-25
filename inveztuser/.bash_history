sudo apt update
sudo apt install python3-pip python3-dev python3-venv libpq-dev nginx curl
sudo apt install mysql-server
sudo mysql_secure_installation
clear
mysql
sudo mysql -u inveztuser 
clear
sudo mysql
sudo mysql -u root -p
clear
mysql -u inveztuser -p
python --version
python3 --version
mkdir ~/invezt
ls
cd invezt/
ls
python3 -m venv env
source env/bin/activate
pip install django gunicorn psycopg2-binary
python3 -m django --version
django-admin startproject website ~/invezt
vi website/settings.py
./manage.py makemigrations
pip install mysqlclient
sudo apt-get install mysqlclient
sudo apt-get install libmysqlclient-dev
sudo apt-get install mysqlclient
pip install mysqlclient
./manage.py makemigrations
./manage.py migrate
./manage.py createsuperuser
./manage.py collectstatic
sudo ufw allow 8000
./manage.py runserver 167.71.176.115
./manage.py runserver 167.71.176.115:8000
gunicorn --bind 0.0.0.0:8000 website.wsgi
deactivate
sudo vi /etc/systemd/system/gunicorn.socket
sudo vi /etc/systemd/system/gunicorn.service
sudo systemctl start gunicorn.socket
sudo systemctl enable gunicorn.service
file /run/gunicorn.sock
curl --unix-socket /run/gunicorn.sock localhost
sudo systemctl status gunicorn
sudo vi /etc/nginx/sites-available/invezt
sudo ln -s /etc/nginx/sites-available/invezt /etc/nginx/sites-enabled
sudo nginx -t
sudo systemctl restart nginx
sudo ufw allow 'Nginx Full'
cd /etc/ssl
sudo cp openssl.cnf selfsigned.cnf
ls
vim openssl.cnf
chmod +w openssl.cnf
sudo chmod +w openssl.cnf
sudo cp openssl.cnf selfsigned.cnf
ls
vim openssl.cnf
sudo vim openssl.cnf
udo openssl req -x509 -days 100 -nodes -newkey rsa:2048 -config selfsigned.cnf -keyout private/selfsigned.key -out certs/selfsigned.cert
sudo openssl req -x509 -days 100 -nodes -newkey rsa:2048 -config selfsigned.cnf -keyout private/selfsigned.key -out certs/selfsigned.cert
sudo openssl x509 -text -in certs/selfsigned.cert -noout
ls
vim openssl.cnf
sudo openssl req -x509 -days 100 -nodes -newkey rsa:2048 -config selfsigned.cnf -keyout private/selfsigned.key -out certs/selfsigned.cert
sudo openssl x509 -text -in certs/selfsigned.cert -noout
cd ../..
ls
cd /etc/ssl
ls
rm selfsigned.cnf
sudo rm selfsigned.cnf
ls
sudo cp openssl.cnf selfsigned.cnf
vim openssl.cnf
sudo vum openssl.cnf
sudo vim openssl.cnf
clear
ls
sudo vim selfsigned.cnf
sudo openssl req -x509 -days 100 -nodes -newkey rsa:2048 -config selfsigned.cnf -keyout private/selfsigned.key -out certs/selfsigned.cert
sudo openssl x509 -text -in certs/selfsigned.cert -noout
cd /etc/nginx/sites-available
ls
sudo vim invezt
server$ sudo nginx -t
sudo nginx -t
sudo systemctl restart nginx
sudo vim invezt
sudo systemctl restart nginx
exit
ls
cd invezt
ls
cd ../
ls
clear
mysql -u inveztuser -p
clear
ls
cd invezt
ls
cd app
ls
cd ..
ls
cd website/
ls
clear
vim settings.py
sudo vi /etc/nginx/sites-enabled/invezt
mkdir ~/chatter/media
cd ..
ls
mkdir ~/invezt/media
cd invezt
ls
chmod a+rx ~/invezt ~/invezt/media
sudo nginx -t
sudo systemctl restart nginx
sudo systemctl restart gunicorn
ls
cd media
ls
clear
curl https://forextraininggroup.com/wp-content/uploads/2016/12/Bear-Flag-Pattern.png
curl https://forextraininggroup.com/wp-content/uploads/2016/12/Bear-Flag-Pattern.png --output bearflag.png
ls
xdg-open bearflag.png
sudo apt install xdg-utils
xdg-open bearflag.png
ls
sudo apt-get install chrome
sudo apt-get install google-chrome
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo apt install ./google-chrome-stable_current_amd64.deb
ls
clear
xdg-open bearflag.png
ls
vim bearflag.png 
google-chrome
eog bearflag.png 
sudo apt-get install eog
eog bearflag.png 
clear
ls
rm google-chrome-stable_current_amd64.deb 
ls
dpkg --list
clear
sudo apt-get --purge remove google-chrome
sudo apt-get --purge remove eog
sudo apt-get --purge remove ./google-chrome-stable_current_amd64.deb
clear
sudo apt-get --purge remove xdg-open
sudo apt-get --purge remove xdg-utils
clear
ls
curl https://forextraininggroup.com/wp-content/uploads/2016/12/Bull-Flag-Pattern-1.png --output bullflag.png
ls
pwd
clear
cd ..
cd media
mysql -u inveztuser -p
ls
tar czf invezt.tgz invezt/app/views.py
ls
chmod a-w chatter.tgz
chmod a-w invezt.tgz
exit
mysql -u inveztuser -p
ls
mysql -u inveztuser -p
git status
vim invezt/app/views.py 
vim invezt/website/urls.py 
sudo systemctl restart gunicorn
sudo systemctl restart nginx
vim invezt/app/views.py 
sudo systemctl restart nginx
sudo systemctl restart gunicorn
vim invezt/app/views.py 
vim invezt/website/urls.py 
sudo systemctl restart nginx
sudo systemctl restart gunicorn
vim invezt/app/views.py 
sudo systemctl restart nginx
sudo systemctl restart gunicorn
vim invezt/website/urls.py 
sudo systemctl restart nginx
sudo systemctl restart gunicorn
vim invezt/app/views.py 
sudo systemctl restart nginx
sudo systemctl restart gunicorn
vim invezt/app/views.py 
ls
cd invezt/
ls
cd app
ls
touch stripe_handler.py
vim stripe_handler.py 
vim views.py
cd ..
vim website/urls.py 
vim app/views.py 
vim website/urls.py 
vim app/views.py 
vim invezt/app/views.py 
mysql -u inveztuser -p
vim invezt/app/views.py 
