package org.example.config.connection.pool;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final String dbUrl;
    private final String userName;
    private final String password;

    private int maxPoolSize = 5;
    private int connectionNum = 0;

    private static final String SQL_VERIFICATION = "select 1";

    Stack<Connection> freePool = new Stack<>();

    ConcurrentLinkedQueue<Connection> occupiedPool = new ConcurrentLinkedQueue<>();

    public ConnectionPool(String dbUrl, String userName, String password, int maxPoolSize) {
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
    }

    public Connection getConnection() throws SQLException {
        lock.lock();
        try {
            Connection connection = createConnection();

            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {

                        String name = method.getName();
                        System.out.println("Called: " + name);
                        System.out.println(method.getName());
                        Object invoke = method.invoke(connection, args);
                        getReleaseConnection(connection);
                        return invoke;
                    }
            );
        } finally {
            lock.unlock();
        }
    }

    public Connection createConnection() throws SQLException {
        Connection dbCconnection = null;
        if (isFull()) {
            throw new SQLException("Connection Pool is full.");
        }
        dbCconnection = getConnectionFromPool();
        if (dbCconnection == null) {
            dbCconnection = createNewConnectionForPool();
        }
        return makeAvailable(dbCconnection);
    }

    private synchronized boolean isFull() {
        return ((freePool.isEmpty()) && (connectionNum >= maxPoolSize));
    }

    public synchronized void returnConnection(Connection dbCconnection) throws SQLException {
        if (dbCconnection == null) {
            throw new NullPointerException();
        }
        if (!occupiedPool.remove(dbCconnection)) {
            throw new SQLException("Connection is already closed.");
        }
        freePool.push(dbCconnection);
    }

    private void getReleaseConnection(Connection connection) {
        lock.lock();
        try {
            occupiedPool.offer(connection);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    private Connection createNewConnectionForPool() throws SQLException {
        Connection connection = createNewConnection();
        connectionNum++;
        occupiedPool.add(connection);
        return connection;
    }

    private Connection createNewConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(dbUrl, userName, password);
        return connection;
    }

    private Connection getConnectionFromPool() throws SQLException {
        Connection connection = null;
        if (freePool.size() > 0) {
            connection = freePool.pop();
            occupiedPool.add(connection);
        }
        return connection;
    }

    private Connection makeAvailable(Connection connection) throws SQLException {
        if (isConnectionAvailable(connection)) {
            return connection;
        } else {
            occupiedPool.remove(connection);
            connectionNum--;
            connection.close();

            connection = createNewConnection();
            occupiedPool.add(connection);
            connectionNum++;
            return connection;
        }
    }

    private boolean isConnectionAvailable(Connection connection) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.execute(SQL_VERIFICATION);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
