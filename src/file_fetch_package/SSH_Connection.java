package file_fetch_package;

import com.jcraft.jsch.Session;

class SSH_Connection extends public_package.SSH_Connection {
    SSH_Connection(Session sess, String env, String user, String pass) {
        super(env, user, pass);
        session=sess;
    }
}
