#!/bin/sh

#Get address of client server
serverip="$(echo $SSH_CLIENT | awk '{ print $1}')"

#Check if router has VSERVER chain present
/usr/sbin/iptables -L -t nat --line-numbers | grep -q "VSERVER"
if [ $? -eq 0 ]
then
    echo "Using VSERVER chain"
    chain="VSERVER"
else
    echo "Using PREROUTING chain.."
    chain="PREROUTING"
fi

#list rules containing minecraft port, and output results into file. 
/usr/sbin/iptables -L -t nat --line-numbers | grep "dpt:25565" | sort -nr >>results.txt

#If rules exist
if [ $? -eq 0 ]
then
    echo "Removing old rules..."
    #get rule numbers from text file and delete them
    while read -r line || [ -n "$line" ]
    do
        id=${line:0:1}
        /usr/sbin/iptables -D $chain $id -t nat
        echo Deleted $chain rule: $line
    done < results.txt
fi

#Append new rules
echo "Adding new rules.."
/usr/sbin/iptables -A $chain -t nat -p udp -m udp --dport 25565 -j DNAT --to $serverip
/usr/sbin/iptables -A $chain -t nat -p tcp -m tcp --dport 25565 -j DNAT --to $serverip
echo "New rules added"

#cleanup
rm results.txt
unset serverip 
unset chain
unset id
unset line

