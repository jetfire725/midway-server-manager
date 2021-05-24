let iptable = {jetfire: "123"};

function updateip(user, oldip, newip){
    if (iptable[user]==oldip){
        iptable[user]=newip;
        return true;
    } else {
        return false;
    }
}

module.exports = {updateip};

