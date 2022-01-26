#!/bin/bash


# cp -r target/classes out/artifacts/StoresOrderingSystem_war_exploded/WEB-INF
# sudo scp -i ~/.ssh/aws.pem -r out/artifacts/StoresOrderingSystem_war_exploded $AWS:/var/lib/tomcat/webapps/StoresOrderingSystem
cp -r target/classes out/artifacts/StoresOrderingSystem_war_exploded/WEB-INF

for AWS_HOST in ec2-54-188-144-96.us-west-2.compute.amazonaws.com ec2-54-71-188-143.us-west-2.compute.amazonaws.com ec2-54-202-215-201.us-west-2.compute.amazonaws.com ec2-54-190-150-196.us-west-2.compute.amazonaws.com
do
FULL_HOST="ec2-user@${AWS_HOST}"
echo " ============= processing $FULL_HOST ============= "
scp -i ~/.ssh/aws.pem /etc/store_proj_cred_aws.properties $FULL_HOST:~
scp -i ~/.ssh/aws.pem -r out/artifacts/StoresOrderingSystem_war_exploded $FULL_HOST:~
ssh -i ~/.ssh/aws.pem $FULL_HOST -t <<EOF
sudo rm /etc/sql_credentials.properties
sudo mv ~/store_proj_cred_aws.properties /etc/store_proj_cred.properties
sudo rm -rf /var/lib/tomcat/webapps/StoresOrderingSystem
sudo mv ~/StoresOrderingSystem_war_exploded /var/lib/tomcat/webapps/
sudo mv /var/lib/tomcat/webapps/StoresOrderingSystem_war_exploded /var/lib/tomcat/webapps/StoresOrderingSystem
sudo systemctl restart tomcat
EOF
done