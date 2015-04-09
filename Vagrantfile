# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  config.vm.box = "ubuntu/trusty64"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  config.vm.network "private_network", ip: "192.168.33.11"
  # config.vm.network "forwarded_port", guest: 9160, guest_ip: "192.168.33.10", host: 9160

  config.vm.network "private_network", ip: "192.168.33.12"
  # config.vm.network "forwarded_port", guest: 9160, guest_ip: "192.168.33.11", host: 9160

  config.vm.network "private_network", ip: "192.168.33.13"
  # config.vm.network "forwarded_port", guest: 9160, guest_ip: "192.168.33.12", host: 9160

  #Rabbitmq ports
  config.vm.network "forwarded_port", guest: 5672, guest_ip: "192.168.33.11", host: 5672
  config.vm.network "forwarded_port", guest: 15672, guest_ip: "192.168.33.11", host: 15672

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "shell", inline: <<-SHELL
   sudo apt-get update
   sudo apt-get -y upgrade
   wget -qO- https://get.docker.com/ | sh #install docker
   sudo usermod -aG docker vagrant #add vagrant user to docker group
   sudo service docker restart #make sure docker is up and running properly

   #ensure rabbitmq directories exist
   sudo mkdir -p /opt/rabbitmq/log
   sudo chown 777 /opt/rabbitmq/log

   sudo mkdir -p /opt/rabbitmq/data
   sudo chown 777 /opt/rabbitmq/data

  config.vm.define "ccm" do |ccm|

   #start rabbitmq docker container for usage. TODO: make a service that will start this container on startup of machine, instead of just "vagrant up"
   sudo docker run -d -e RABBITMQ_NODENAME=nurf --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

   #Note: this step is skipped for now, as the cassandra cluster needs to persist for days, and had been started locally for me.
   #setup cassandra (using ccm)
  #  sudo apt-get install -y python-setuptools
  #  sudo easy_install pyYaml
  #  sudo easy_install six
  #  sudo apt-get install -y ant
  #  sudo easy_install pip
  #  sudo pip install ccm
  #  ccm status
  #  ccm create nurf -v 2.0.5 -n 3 -i 192.168.33.1 #make sure to install ccm as vagrant user, for ease of use. TODO: create a newest version of cassandra?
  #  ccm status
 SHELL


end
