package phoenix.delta;

public class DUser
{
    private String m_username, m_password;
    private UserType m_type;
    private int m_id;

    public int getId()
    {
        return m_id;
    }

    public void setId(int p_id)
    {
        m_id = p_id;
    }

    public DUser(String p_username, String p_password, UserType p_type)
    {
        m_username = p_username;
        m_password = p_password;
        m_type = p_type;
    }

    public String getUsername()
    {
        return m_username;
    }

    public boolean checkPassword (String p_pw)
    {
        return (p_pw.compareTo(m_password) == 0);
    }

    public String toString ()
    {
        return m_username + "," + m_password + "," + m_type;
    }
}