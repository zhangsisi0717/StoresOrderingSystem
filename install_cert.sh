#!/bin/bash

# for AWS_HOST in ec2-54-188-144-96.us-west-2.compute.amazonaws.com ec2-54-71-188-143.us-west-2.compute.amazonaws.com ec2-54-202-215-201.us-west-2.compute.amazonaws.com ec2-54-190-150-196.us-west-2.compute.amazonaws.com
# do
# FULL_HOST="ec2-user@${AWS_HOST}"
# echo " ============= importing cert for $FULL_HOST ============= "
# scp -i ~/.ssh/aws.pem import_cert_linux.sh $FULL_HOST:~
# ssh -i ~/.ssh/aws.pem $FULL_HOST -t <<EOF
# cd ~
# sudo chmod +x import_cert_linux.sh
# sudo ./import_cert_linux.sh
# ls /etc/certs
# EOF
# done

for AWS_HOST in ec2-54-188-144-96.us-west-2.compute.amazonaws.com ec2-54-71-188-143.us-west-2.compute.amazonaws.com ec2-54-202-215-201.us-west-2.compute.amazonaws.com ec2-54-190-150-196.us-west-2.compute.amazonaws.com
do
FULL_HOST="ec2-user@${AWS_HOST}"
echo " ============= changing tomcat.conf for $FULL_HOST ============= "
scp -i ~/.ssh/aws.pem tomcat.conf $FULL_HOST:~
ssh -i ~/.ssh/aws.pem $FULL_HOST -t <<EOF
sudo mv -v ~/tomcat.conf /usr/share/tomcat/conf/tomcat.conf
cat /usr/share/tomcat/conf/tomcat.conf | grep "JAVA_OPTS"
sudo systemctl restart tomcat
EOF
done