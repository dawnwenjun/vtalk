package com.server.dao;

import com.talk.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class userDao {


    //用户登录
    public boolean login(String username,String password){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
            String sql="select * from users where uname=? and upw=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            rs=ps.executeQuery();

            if(rs.next()){
                return true;
            }

        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放数据集对象
            if(rs!=null) {
                try {
                    rs.close();
                    rs=null;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                    ps=null;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

   //用户注册
    public int register(String username, String password, String nickname,String autograph){
        Connection conn=null;
        PreparedStatement ps=null;
        int rs;
        try {
            conn= DBHelper.getConnection();
            String sql="insert into users(uname,upw,nickname,autograph,imgurl) values(?,?,?,?,?)";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ps.setString(3,nickname);
            ps.setString(4,autograph);
            ps.setString(5,"qq.jpg");
            rs=ps.executeUpdate();
            return rs;
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //根据name查询个人信息
     public Map<String, Object> selectByname(String username){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
            String sql="select * from users where uname=?";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            rs=ps.executeQuery();
            Map<String, Object> row = new HashMap<>();
            while (rs.next()){
                row.put("uname", rs.getString("uname"));
                row.put("upw", rs.getString("upw"));
                row.put("nickname", rs.getString("nickname"));
                row.put("autograph", rs.getString("autograph"));
                row.put("imgurl", rs.getString("imgurl"));
            }
            return row;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //根据name查询好友个人信息  数组对象
    public ArrayList<Map<String, Object>> selectFriendByname(String username){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
            System.out.println(username);
            String sql="select uname,nickname,autograph,imgurl from users where uname in (select friendname from friends where username=?);";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            rs=ps.executeQuery();
            ArrayList<Map<String, Object>> ads = new ArrayList<>();

            while (rs.next()){
                Map<String, Object> row = new HashMap<>();
                row.put("uname", rs.getString("uname"));
                row.put("nickname", rs.getString("nickname"));
                row.put("autograph", rs.getString("autograph"));
                row.put("imgurl", rs.getString("imgurl"));
                ads.add(row);
            }
            return ads;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //根据群号获取群所有信息
    public ArrayList<Map<String, Object>> getGroupsinfoByname(String username){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
            System.out.println(username);
            String sql="select groupid,groupName,groupImgurl,groupAbout,groupNum from usercongroup,groups where username=? and groupid=groups.id;";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            rs=ps.executeQuery();
            ArrayList<Map<String, Object>> ads = new ArrayList<>();
            while (rs.next()){
                Map<String, Object> row = new HashMap<>();
                row.put("groupid", rs.getString("groupid"));
                row.put("groupName", rs.getString("groupName"));
                row.put("groupImgurl", rs.getString("groupImgurl"));
                row.put("groupAbout", rs.getString("groupAbout"));
                row.put("groupNum", rs.getString("groupNum"));
                ads.add(row);
            }
            return ads;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<Map<String, Object>> getGroupsnumberByindex(int index){

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
//            System.out.println(username);
            String sql="select username,nickname,autograph,imgurl FROM usercongroup,users where groupid=? and usercongroup.username=users.uname;";
            ps=conn.prepareStatement(sql);
            ps.setInt(1,index);
            rs=ps.executeQuery();
            ArrayList<Map<String, Object>> ads = new ArrayList<>();
            while (rs.next()){
                Map<String, Object> row = new HashMap<>();
                row.put("username", rs.getString("username"));
                row.put("nickname", rs.getString("nickname"));
                row.put("autograph", rs.getString("autograph"));
                row.put("imgurl", rs.getString("imgurl"));
                ads.add(row);
            }
            return ads;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //判断是一个人是不是另一个人好友
    public boolean checkIsFriends(String username,String friendname){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
//            System.out.println(username);
            String sql="select * from friends  where  username=? and friendname=?;";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,friendname);
            rs=ps.executeQuery();

            if(rs.next()){
                return true;
            }


        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public List<String> selectfriendname(String username){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
            String sql="select friendname from friends where username=?;";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            rs=ps.executeQuery();
            List<String> res=new ArrayList<String>();
           while(rs.next()){
//                System.out.println(rs.getString("friendname"));
               String friendname=rs.getString("friendname");
               res.add(friendname);
           }
            return  res;
        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public void addfriend(String username, String friendname) {
        Connection conn=null;
        PreparedStatement ps=null;
        PreparedStatement ps1=null;

        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();

            String sql="insert into friends (username,friendname) values (?,?);";
            ps=conn.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,friendname);
            ps.executeUpdate();

            String sql1="insert into friends (username,friendname) values (?,?);";
            ps1=conn.prepareStatement(sql1);
            ps1.setString(1,friendname);
            ps1.setString(2,username);
            ps1.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean clickGroup(String username, String groupid) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
//            System.out.println(username);
            String sql="select * from groups,usercongroup  where  groups.id=usercongroup.groupid and groups.groupNum=? and username=? ;";
            ps=conn.prepareStatement(sql);
            ps.setString(1,groupid);
            ps.setString(2,username);
            rs=ps.executeQuery();

            if(rs.next()){
                return true;
            }


        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public Map<String, Object> isGroup(String groupid) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn= DBHelper.getConnection();
            String sql="select * from groups where groupNum=? ;";
            ps=conn.prepareStatement(sql);
            ps.setString(1,groupid);
            rs=ps.executeQuery();
            Map<String, Object> row = new HashMap<>();

            while (rs.next()){
                row.put("groupid", rs.getString("id"));
                row.put("groupName", rs.getString("groupName"));
                row.put("groupImgurl", rs.getString("groupImgurl"));
                row.put("groupAbout", rs.getString("groupAbout"));
                row.put("groupNum", rs.getString("groupNum"));
            }
            return row;
        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void addgroup(String username, String groupid) {
        Connection conn=null;
        PreparedStatement ps=null;

        try {
            conn= DBHelper.getConnection();
            String sql="insert into usercongroup (groupid,username) values (?,?);";
            ps=conn.prepareStatement(sql);
            ps.setString(1,groupid);
            ps.setString(2,username);
            ps.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void updateUserImg(String username, String imgurl) {
        Connection conn=null;
        PreparedStatement ps=null;

        try {
            conn= DBHelper.getConnection();
            String sql="update users set imgurl=? where uname=?;";
            ps=conn.prepareStatement(sql);
            ps.setString(1,imgurl);
            ps.setString(2,username);
            ps.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            //释放语句对象
            if(ps!=null) {
                try {
                    ps.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        userDao userdao = new userDao();
        userdao.selectfriendname("0401");
    }

}
