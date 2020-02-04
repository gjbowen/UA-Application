package concur_package;

import com.jcraft.jsch.Session;

class SSH extends public_package.SSH_Connection {
    SSH(Session sess, String user, String pass, String env) {
        super(user, pass,env);
        session=sess;
    }

}
