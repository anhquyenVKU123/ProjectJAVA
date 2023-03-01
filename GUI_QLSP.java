package GUISP;
import GUILogin_And_Register.GUILogin;
import GUISP.GUI_QLSP;
import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GUI_QLSP extends javax.swing.JFrame {
    //Khai báo các thành phần kết nối CSDL
    String URL = "jdbc:sqlserver://localhost:1433;databaseName=Product;user=sa;password=quyen230504";
    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    //Khai báo các thuộc tính
    String idSP, nameSP, nhaSX,namSX,loaiSP, xuatxu, gianhapkho, giaban,daban, dangban, GNK,GB;
    DefaultTableModel model;
    //**Phụ ---- Phương thức thêm dấu chấm vào trong giá tiền
    public StringBuilder addDot(String s){
        StringBuilder strBuilder = new StringBuilder(s);
        int i = strBuilder.length() - 1, j;
        j = i - 2;
        while( i > 0 ){
            if ( i == j ){
                strBuilder.insert(j, '.');
                i--;
                j = i - 2;
            } else i--;
        }
        return strBuilder;
    }
    //Phương thức thêm sản phẩm vào bảng và CSDL
    public boolean addProduct(){
        model = (DefaultTableModel) jTable1.getModel();
        idSP = idSP_text.getText();
        nameSP = nameSP_text.getText();
        nhaSX = nhaSX_text.getText();
        namSX = namSX_combobox.getSelectedItem().toString();
        loaiSP = loaiSP_combobox.getSelectedItem().toString();
        xuatxu = xuatxu_combobox.getSelectedItem().toString();
        gianhapkho = giaNhap_text.getText();
            GNK = giaNhap_text.getText();
        giaban = giaBan_text.getText();
            GB = giaBan_text.getText();
        daban = daBan_text.getText();
        dangban = dangBan_text.getText();
        //Xóa bỏ các dấu chấm phẩy trong ô giá nhập kho và giá bán trước khi đưa vào cơ sở dữ liệu
        for (int i = 0 ; i < gianhapkho.length(); i++)
            if ( gianhapkho.charAt(i) == '.') gianhapkho = gianhapkho.substring(0,i) + gianhapkho.substring(i+1);
        for (int i = 0 ; i < giaban.length(); i++)
            if ( giaban.charAt(i) == '.') giaban = giaban.substring(0,i) + giaban.substring(i+1);
        //Thêm dữ liệu vào cơ sở dữ liệu
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement("insert into Information (ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban) values(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, idSP);
            ps.setString(2, nameSP);
            ps.setString(3, nhaSX);
            ps.setInt(4,Integer.valueOf(namSX));
            ps.setString(5,loaiSP);
            ps.setString(6, xuatxu);
            ps.setInt(7, Integer.valueOf(gianhapkho));
            ps.setInt(8, Integer.valueOf(giaban));
            ps.setInt(9, Integer.valueOf(daban));
            ps.setInt(10,Integer.valueOf(dangban));
            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    //Phương thức xóa một sản phẩm ra khỏi bảng và CSDL
    public void deleteProduct(){
        if ( jTable1.getSelectedRowCount() == 1){
            try {
                Class.forName(driver);
                Connection conn = DriverManager.getConnection(URL);
                int index = jTable1.getSelectedRow();
                String id_deleted = jTable1.getModel().getValueAt(index, 0).toString();
                //Hỏi trước khi xóa một sản phẩm
                int ans = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn xóa sản phẩm này không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
                if (ans == 0 ){
                    PreparedStatement ps = conn.prepareStatement("delete from Information where ID = '" + id_deleted + "'");
                    ps.executeUpdate();
                    model.removeRow(index);
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex){
                ex.printStackTrace();
            }
           
         } else {
             if (jTable1.getRowCount() == 0 ){
                 JOptionPane.showMessageDialog(rootPane, "Không có dữ liệu", "Thông báo",JOptionPane.INFORMATION_MESSAGE);
             } else JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn một dòng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         }
    }
    //Phương thức hiển thị tất cả các dữ liệu từ SQL ra Table
    public void showAll(){
        try{
            jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select*from Information");
            model = (DefaultTableModel) jTable1.getModel();            
            while (rs.next()){      
                idSP = rs.getString("ID");
                nameSP = rs.getString("nameSP");
                nhaSX = rs.getString("nhaSX");
                namSX = String.valueOf(rs.getInt("namSX"));
                loaiSP = rs.getString("loaiSP");
                xuatxu = rs.getString("xuatxu");
                gianhapkho = String.valueOf(rs.getInt("gianhapkho"));
                giaban = String.valueOf(rs.getInt("giaban"));
                daban = String.valueOf(rs.getInt("daban"));
                dangban = String.valueOf(rs.getInt("dangban"));
                //Thêm dấu chấm vào trong giá tiền trước khi xuất ra bảng dữ liệu
                StringBuilder str1 = new StringBuilder(gianhapkho);
                str1 = addDot(gianhapkho);
                StringBuilder str2 = new StringBuilder(giaban);
                str2 = addDot(giaban);
                //Đưa dữ liệu từ CSDL vào trong Table              
                Object []row = {idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,str1,str2,daban,dangban};
                model.addRow(row);
            }
        } catch(ClassNotFoundException ex) {
                ex.printStackTrace();
        } catch (SQLException ex){
                ex.printStackTrace();
        }        
    }
   //Phương thức xóa hết tất cả dữ liệu từ SQL và trong bảng
    public void deleteAll(){
        try{
            if (jTable1.getRowCount() == 0 ){
                JOptionPane.showMessageDialog(rootPane, "Không có dữ liệu","Thông báo",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            st.executeUpdate("delete from Information");
            jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
        } catch(ClassNotFoundException ex) {
                ex.printStackTrace();
        } catch (SQLException ex){
                ex.printStackTrace();
        }
    }
    //Phương thức làm mới dữ liệu nhập vào nhưng không xóa dữ liệu trong cơ sở dữ liệu
    public void resetData(){
        jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
    }
    //Sự kiện cập nhật thông tin sản phẩm trong cơ sở dữ liệu và bảng
    public boolean updateInformation(){
            idSP = idSP_text.getText();
            nameSP = nameSP_text.getText();
            nhaSX = nhaSX_text.getText();
            namSX = namSX_combobox.getSelectedItem().toString();
            loaiSP = loaiSP_combobox.getSelectedItem().toString();
            xuatxu = xuatxu_combobox.getSelectedItem().toString();
            gianhapkho = giaNhap_text.getText();
            giaban = giaBan_text.getText();
            daban = daBan_text.getText();
            dangban = dangBan_text.getText();
            model = (DefaultTableModel) jTable1.getModel();
            String oldID = model.getValueAt(jTable1.getSelectedRow(), 0).toString();
            //Xóa dấu chấm của giá tiền trước khi đưa vào cơ sở dữ liệu
            for (int i = 0 ; i < gianhapkho.length(); i++)
                if ( gianhapkho.charAt(i) == '.') gianhapkho = gianhapkho.substring(0,i) + gianhapkho.substring(i+1);
            for (int i = 0 ; i < giaban.length(); i++)
                if ( giaban.charAt(i) == '.') giaban = giaban.substring(0,i) + giaban.substring(i+1);
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            PreparedStatement ps = conn.prepareStatement("update Information set  "
                    + "ID = '" +idSP+ "', nameSP = N'" +nameSP+ "', nhaSX = N'" +nhaSX+ "', namSX = " +Integer.parseInt(namSX)+ ", loaiSP = N'" +loaiSP+ "', xuatxu = N'"+xuatxu
                    + "', gianhapkho = " +Integer.parseInt(gianhapkho)+ ", giaban = " +Integer.parseInt(giaban)+ ", daban = " +Integer.parseInt(daban)+ ", dangban = "+ Integer.parseInt(dangban)
            + "where ID = '" +oldID+ "'");
            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    //Phương thức tìm kiếm thông tin sản phẩm
    public void searchInfor(int a, String s1, String s2){       
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban from Information where namSX = " + a + " and loaiSP = N'" + s1 + "' and xuatxu = N'" + s2 + "'");
            model = (DefaultTableModel) jTable1.getModel();
            while (rs.next()){
                idSP = rs.getString("ID");
                nameSP = rs.getString("nameSP");
                nhaSX = rs.getString("nhaSX");
                namSX = String.valueOf(rs.getInt("namSX"));
                loaiSP = rs.getString("loaiSP");
                xuatxu = rs.getString("xuatxu");
                gianhapkho = String.valueOf(rs.getInt("gianhapkho"));
                giaban = String.valueOf(rs.getInt("giaban"));
                daban = String.valueOf(rs.getInt("daban"));
                dangban = String.valueOf(rs.getInt("dangban"));
                //Thêm dấu chấm vào trong giá tiền trước khi xuất ra bảng dữ liệu
                StringBuilder str1 = new StringBuilder(gianhapkho);
                str1 = addDot(gianhapkho);
                StringBuilder str2 = new StringBuilder(giaban);
                str2 = addDot(giaban);
                //Đưa dữ liệu từ CSDL vào trong Table              
                Object []row = {idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,str1,str2,daban,dangban};
                model.addRow(row);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //Phương thức sắp xếp dữ liệu
    public void sort(String property, String type){
        jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban from Information order by " + property + " " + type);
            model = (DefaultTableModel) jTable1.getModel();
            while (rs.next()){
                idSP = rs.getString("ID");
                nameSP = rs.getString("nameSP");
                nhaSX = rs.getString("nhaSX");
                namSX = String.valueOf(rs.getInt("namSX"));
                loaiSP = rs.getString("loaiSP");
                xuatxu = rs.getString("xuatxu");
                gianhapkho = String.valueOf(rs.getInt("gianhapkho"));
                giaban = String.valueOf(rs.getInt("giaban"));
                daban = String.valueOf(rs.getInt("daban"));
                dangban = String.valueOf(rs.getInt("dangban"));
                //Thêm dấu chấm vào trong giá tiền trước khi xuất ra bảng dữ liệu
                StringBuilder str1 = new StringBuilder(gianhapkho);
                str1 = addDot(gianhapkho);
                StringBuilder str2 = new StringBuilder(giaban);
                str2 = addDot(giaban);
                //Đưa dữ liệu từ CSDL vào trong Table              
                Object []row = {idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,str1,str2,daban,dangban};
                model.addRow(row);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //**Phụ ---- chuyển đổi dữ liệu trong jComboBox ở hộp thoại
    public void getData(String property, String type){
        if( property.equals("Năm sản xuất") && type.equals("Tăng dần")) {
            sort("namSX","ASC");
            return;
        }
        if( property.equals("Năm sản xuất") && type.equals("Giảm dần")) {
            sort("namSX","DESC");
            return;
        } 
        
        if( property.equals("Giá nhập kho") && type.equals("Tăng dần")) {
            sort("gianhapkho","ASC");
            return;
        }
        if( property.equals("Giá nhập kho") && type.equals("Giảm dần")) {
            sort("gianhapkho","DESC");
            return;
        }
        
        if( property.equals("Giá bán") && type.equals("Tăng dần")) {
            sort("giaban","ASC");
            return;
        }
        if( property.equals("Giá bán") && type.equals("Giảm dần")) {
            sort("giaban","DESC");
            return;
        }
        
        if( property.equals("Đã bán") && type.equals("Tăng dần")) {
            sort("daban","ASC");
            return;
        }
        if( property.equals("Đã bán") && type.equals("Giảm dần")) {
            sort("daban","DESC");
            return;
        }
        
        if( property.equals("Đang bán") && type.equals("Tăng dần")) {
            sort("dangban","ASC");
            return;
        }
        if( property.equals("Đang bán") && type.equals("Giảm dần")) {
            sort("dangban","DESC");
            return;
        }
    }
    //Constructor tạo giao diện QLNV
    public GUI_QLSP() {
        initComponents();
        setLocationRelativeTo(null);
    }

    //Bố trí các thành phần trong giao diện QLSP
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        title_panel = new javax.swing.JPanel();
        title_label = new javax.swing.JLabel();
        hide_button = new javax.swing.JButton();
        close_button = new javax.swing.JButton();
        in4_panel = new javax.swing.JPanel();
        in4_label = new javax.swing.JLabel();
        idSP_label = new javax.swing.JLabel();
        nameSP_label = new javax.swing.JLabel();
        nhaSX_label = new javax.swing.JLabel();
        namSX_label = new javax.swing.JLabel();
        loai_label = new javax.swing.JLabel();
        xuatxu_label = new javax.swing.JLabel();
        tiennhap_label = new javax.swing.JLabel();
        tienban_label = new javax.swing.JLabel();
        daban_label = new javax.swing.JLabel();
        dangban_label = new javax.swing.JLabel();
        idSP_text = new javax.swing.JTextField();
        nameSP_text = new javax.swing.JTextField();
        nhaSX_text = new javax.swing.JTextField();
        giaNhap_text = new javax.swing.JTextField();
        giaBan_text = new javax.swing.JTextField();
        daBan_text = new javax.swing.JTextField();
        dangBan_text = new javax.swing.JTextField();
        namSX_combobox = new javax.swing.JComboBox<>();
        loaiSP_combobox = new javax.swing.JComboBox<>();
        xuatxu_combobox = new javax.swing.JComboBox<>();
        tongSP_label = new javax.swing.JLabel();
        tongSP_text = new javax.swing.JTextField();
        currency1_label = new javax.swing.JLabel();
        currency2_label = new javax.swing.JLabel();
        searchYear_button = new javax.swing.JButton();
        searchType_button = new javax.swing.JButton();
        searchOrigin_button = new javax.swing.JButton();
        sort_button = new javax.swing.JButton();
        func_panel = new javax.swing.JPanel();
        add_button = new javax.swing.JButton();
        update_button = new javax.swing.JButton();
        delete_button = new javax.swing.JButton();
        deleteAll_button = new javax.swing.JButton();
        reset_button = new javax.swing.JButton();
        showAll_button = new javax.swing.JButton();
        scrollpane_table = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản lí sản phẩm");
        setUndecorated(true);

        title_panel.setBackground(new java.awt.Color(0, 51, 51));
        title_panel.setForeground(new java.awt.Color(242, 242, 242));

        title_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        title_label.setForeground(new java.awt.Color(255, 255, 255));
        title_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title_label.setText("QUẢN LÍ SẢN PHẨM");

        hide_button.setBackground(new java.awt.Color(0, 51, 51));
        hide_button.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        hide_button.setForeground(new java.awt.Color(255, 255, 255));
        hide_button.setText("-");
        hide_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hide_buttonActionPerformed(evt);
            }
        });

        close_button.setBackground(new java.awt.Color(0, 51, 51));
        close_button.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        close_button.setForeground(new java.awt.Color(255, 255, 255));
        close_button.setText("x");
        close_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout title_panelLayout = new javax.swing.GroupLayout(title_panel);
        title_panel.setLayout(title_panelLayout);
        title_panelLayout.setHorizontalGroup(
            title_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(title_panelLayout.createSequentialGroup()
                .addComponent(title_label, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(hide_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(close_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        title_panelLayout.setVerticalGroup(
            title_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(title_panelLayout.createSequentialGroup()
                .addGroup(title_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hide_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(close_button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(title_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        in4_panel.setBackground(new java.awt.Color(0, 102, 102));

        in4_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        in4_label.setForeground(new java.awt.Color(255, 255, 255));
        in4_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        in4_label.setText("THÔNG TIN SẢN PHẨM");

        idSP_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        idSP_label.setForeground(new java.awt.Color(255, 255, 255));
        idSP_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        idSP_label.setText("Mã sản phẩm");

        nameSP_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        nameSP_label.setForeground(new java.awt.Color(255, 255, 255));
        nameSP_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nameSP_label.setText("Tên sản phẩm");

        nhaSX_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        nhaSX_label.setForeground(new java.awt.Color(255, 255, 255));
        nhaSX_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nhaSX_label.setText("Nhà SX");

        namSX_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        namSX_label.setForeground(new java.awt.Color(255, 255, 255));
        namSX_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        namSX_label.setText("Năm SX");

        loai_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        loai_label.setForeground(new java.awt.Color(255, 255, 255));
        loai_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loai_label.setText("Loại");

        xuatxu_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        xuatxu_label.setForeground(new java.awt.Color(255, 255, 255));
        xuatxu_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        xuatxu_label.setText("Xuất xứ");

        tiennhap_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        tiennhap_label.setForeground(new java.awt.Color(255, 255, 255));
        tiennhap_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tiennhap_label.setText("Giá nhập kho");

        tienban_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        tienban_label.setForeground(new java.awt.Color(255, 255, 255));
        tienban_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tienban_label.setText("Giá bán");

        daban_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        daban_label.setForeground(new java.awt.Color(255, 255, 255));
        daban_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        daban_label.setText("Đã bán");

        dangban_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        dangban_label.setForeground(new java.awt.Color(255, 255, 255));
        dangban_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dangban_label.setText("Đang bán");

        idSP_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        nameSP_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        nhaSX_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        giaNhap_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        giaBan_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        daBan_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        dangBan_text.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        namSX_combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022", "2023" }));

        loaiSP_combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CPU", "Bàn phím", "Chuột", "Thiết bị lưu trữ", "Card đồ họa" }));

        xuatxu_combobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Việt Nam", "Trung Quốc", "Thái Lan", "Nhật Bản", "Singapore", "Anh", "Pháp", "Mỹ" }));

        tongSP_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        tongSP_label.setForeground(new java.awt.Color(255, 255, 255));
        tongSP_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tongSP_label.setText("Tổng số sản phẩm");

        tongSP_text.setEditable(false);
        tongSP_text.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tongSP_text.setText("0");

        currency1_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        currency1_label.setForeground(new java.awt.Color(255, 255, 255));
        currency1_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currency1_label.setText("VNĐ");

        currency2_label.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        currency2_label.setForeground(new java.awt.Color(255, 255, 255));
        currency2_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currency2_label.setText("VNĐ");

        searchYear_button.setBackground(new java.awt.Color(255, 102, 0));
        searchYear_button.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        searchYear_button.setForeground(new java.awt.Color(255, 255, 255));
        searchYear_button.setText("Tìm");
        searchYear_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchYear_buttonActionPerformed(evt);
            }
        });

        searchType_button.setBackground(new java.awt.Color(255, 102, 0));
        searchType_button.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        searchType_button.setForeground(new java.awt.Color(255, 255, 255));
        searchType_button.setText("Tìm");
        searchType_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchType_buttonActionPerformed(evt);
            }
        });

        searchOrigin_button.setBackground(new java.awt.Color(255, 102, 0));
        searchOrigin_button.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        searchOrigin_button.setForeground(new java.awt.Color(255, 255, 255));
        searchOrigin_button.setText("Tìm");
        searchOrigin_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOrigin_buttonActionPerformed(evt);
            }
        });

        sort_button.setBackground(new java.awt.Color(255, 204, 0));
        sort_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        sort_button.setText("Sắp xếp dữ liệu");
        sort_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sort_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout in4_panelLayout = new javax.swing.GroupLayout(in4_panel);
        in4_panel.setLayout(in4_panelLayout);
        in4_panelLayout.setHorizontalGroup(
            in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(in4_panelLayout.createSequentialGroup()
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(in4_panelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(in4_label, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(in4_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(idSP_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(idSP_text, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(in4_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nameSP_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nameSP_text, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(in4_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sort_button)
                            .addGroup(in4_panelLayout.createSequentialGroup()
                                .addComponent(tongSP_label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tongSP_text, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(90, 90, 90)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(in4_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(in4_panelLayout.createSequentialGroup()
                        .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(loai_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(loaiSP_combobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(namSX_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(namSX_combobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(nhaSX_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(nhaSX_text, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchYear_button, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchType_button, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(in4_panelLayout.createSequentialGroup()
                        .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(xuatxu_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(xuatxu_combobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(dangban_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(dangBan_text))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(daban_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(daBan_text))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(tienban_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(giaBan_text))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, in4_panelLayout.createSequentialGroup()
                                .addComponent(tiennhap_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(giaNhap_text, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(in4_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(currency1_label, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(currency2_label, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(in4_panelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(searchOrigin_button, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        in4_panelLayout.setVerticalGroup(
            in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(in4_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(in4_label, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idSP_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idSP_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameSP_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameSP_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nhaSX_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nhaSX_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namSX_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namSX_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchYear_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loai_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loaiSP_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchType_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(xuatxu_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(xuatxu_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchOrigin_button)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiennhap_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(giaNhap_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currency1_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tienban_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(giaBan_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currency2_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(daban_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(daBan_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dangban_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dangBan_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(in4_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tongSP_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tongSP_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sort_button, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        func_panel.setBackground(new java.awt.Color(0, 153, 51));
        func_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2), "Dữ liệu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        add_button.setBackground(new java.awt.Color(255, 255, 255));
        add_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        add_button.setForeground(new java.awt.Color(0, 153, 0));
        add_button.setText("Thêm");
        add_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_buttonActionPerformed(evt);
            }
        });

        update_button.setBackground(new java.awt.Color(255, 255, 255));
        update_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        update_button.setForeground(new java.awt.Color(0, 153, 0));
        update_button.setText("Cập nhật");
        update_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_buttonActionPerformed(evt);
            }
        });

        delete_button.setBackground(new java.awt.Color(255, 255, 255));
        delete_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        delete_button.setForeground(new java.awt.Color(0, 153, 0));
        delete_button.setText("Xóa");
        delete_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_buttonActionPerformed(evt);
            }
        });

        deleteAll_button.setBackground(new java.awt.Color(255, 255, 255));
        deleteAll_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        deleteAll_button.setForeground(new java.awt.Color(0, 153, 0));
        deleteAll_button.setText("Xóa hết dữ liệu");
        deleteAll_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAll_buttonActionPerformed(evt);
            }
        });

        reset_button.setBackground(new java.awt.Color(255, 255, 255));
        reset_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        reset_button.setForeground(new java.awt.Color(0, 153, 0));
        reset_button.setText("Làm mới dữ liệu");
        reset_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_buttonActionPerformed(evt);
            }
        });

        showAll_button.setBackground(new java.awt.Color(255, 255, 255));
        showAll_button.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        showAll_button.setForeground(new java.awt.Color(0, 153, 0));
        showAll_button.setText("Toàn bộ sản phẩm");
        showAll_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAll_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout func_panelLayout = new javax.swing.GroupLayout(func_panel);
        func_panel.setLayout(func_panelLayout);
        func_panelLayout.setHorizontalGroup(
            func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(func_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(add_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showAll_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(deleteAll_button, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update_button, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(168, 168, 168)
                .addGroup(func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delete_button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reset_button, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(32, 32, 32))
        );
        func_panelLayout.setVerticalGroup(
            func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(func_panelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add_button)
                    .addComponent(update_button)
                    .addComponent(delete_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(func_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showAll_button)
                    .addComponent(deleteAll_button, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reset_button, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        scrollpane_table.setForeground(new java.awt.Color(0, 204, 204));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jTable1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Nhà SX", "Năm SX", "Loại", "Xuất xứ", "Giá nhập kho", "Giá bán", "Đã bán", "Đang bán"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });

        jScrollPane3.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(600);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(600);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(400);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(400);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(200);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 2029, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        scrollpane_table.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(in4_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollpane_table, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
                    .addComponent(func_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(title_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollpane_table, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(func_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(in4_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>                        

    //Sự kiện ẩn giao diện QLSP
    private void hide_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        setState(GUI_QLSP.ICONIFIED);
    }                                           
    //Sự kiện đóng giao diện QLSP   
    private void close_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        int ans = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn đóng ứng dụng không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if ( ans == 0 ){
            System.exit(0);
        }  
    }                                            

   //Sự kiện thêm sản phẩm vào bảng và CSDL
    private void add_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        //Kiểm tra đã điền đầy đủ thông tin sản phẩm chưa
        if (idSP_text.getText().equals("") ||nameSP_text.getText().equals("") || 
            nhaSX_text.getText().equals("") ||giaNhap_text.getText().equals("") ||
            giaBan_text.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Chưa nhập đầy đủ thông tin của sản phẩm");
            if ( daBan_text.getText().equals("") ) daBan_text.setText("0");
            if ( dangBan_text.getText().equals("")) dangBan_text.setText("0");
        } 
        else 
            //Nếu nhập thông tin đầy đủ rồi thì tiến hành thêm dữ liệu
            if (addProduct()) {
                JOptionPane.showMessageDialog(null, "Success");
                model.addRow(new Object[]{idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,GNK,GB,dangban,daban});
                tongSP_text.setText(String.valueOf(jTable1.getRowCount()));
            }else 
                JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại hoặc quá dài ( tối đa 10 kí tự )", "Lỗi", JOptionPane.ERROR_MESSAGE);             
    }                                          
    //Sự kiện xóa một sản phẩm ra khỏi bảng và CSDL
    private void delete_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        deleteProduct();
        tongSP_text.setText(String.valueOf(jTable1.getRowCount()));
    }                                             
    //Sự kiện nhấp chuột vào một dòng và hiển thị thông tin
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {                                     
        model = (DefaultTableModel) jTable1.getModel();
        int row = jTable1.getSelectedRow();
        idSP_text.setText(model.getValueAt(row, 0).toString());
        nameSP_text.setText(model.getValueAt(row, 1).toString());
        nhaSX_text.setText(model.getValueAt(row, 2).toString());
        
        //Lấy giá trị từ bảng hiển thị sang ComboBox
        String combo_namSX = model.getValueAt(row, 3).toString();
        for (int i = 0; i < namSX_combobox.getItemCount(); i++)
            if ( namSX_combobox.getItemAt(i).toString().equalsIgnoreCase(combo_namSX))
                namSX_combobox.setSelectedIndex(i);
        
        String combo_loaiSP = model.getValueAt(row, 4).toString();
        for (int i = 0; i < loaiSP_combobox.getItemCount(); i++)
            if ( loaiSP_combobox.getItemAt(i).toString().equalsIgnoreCase(combo_loaiSP))
                loaiSP_combobox.setSelectedIndex(i);
        
        String combo_xuatxu = model.getValueAt(row, 5).toString();
        for (int i = 0; i < xuatxu_combobox.getItemCount(); i++)
            if ( xuatxu_combobox.getItemAt(i).toString().equalsIgnoreCase(combo_xuatxu))
                xuatxu_combobox.setSelectedIndex(i);
        
        giaNhap_text.setText(model.getValueAt(row, 6).toString());
        giaBan_text.setText(model.getValueAt(row, 7).toString());
        daBan_text.setText(model.getValueAt(row, 8).toString());
        dangBan_text.setText(model.getValueAt(row, 9).toString());
        
    }                                    

   //Sự kiện xóa hết dữ liệu trong cơ sở dữ liệu và trong bảng
    private void deleteAll_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        int ans = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa hết sản phẩm không ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if ( ans == 0 ){
            deleteAll();
            tongSP_text.setText(String.valueOf(jTable1.getRowCount()));
        }
    }                                                
    //Sự kiện làm mới dữ liệu trên giao diện nhưng không xóa dữ liệu trong cơ sở dữ liệu
    private void reset_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        resetData();
    }                                            

   //Sự kiện cập nhật thông tin sản phẩm
    private void update_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        //Kiểm tra xem có dòng nào được chọn chưa
        if ( jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Không có dữ liệu để cập nhật","Lỗi",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Hộp thoại xác nhận cập nhật thông tin
        int ans = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn thay đổi thông tin sản phẩm ?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if ( ans == 0 ){
            if (updateInformation()){
                JOptionPane.showMessageDialog(null, "Thông tin sản phẩm đã được thay đổi","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                model = (DefaultTableModel) jTable1.getModel();
                int row = jTable1.getSelectedRow();
                model.setValueAt(idSP_text.getText(), row, 0);
                model.setValueAt(nameSP_text.getText(), row, 1);
                model.setValueAt(nhaSX_text.getText(), row, 2);
                model.setValueAt(Integer.valueOf(namSX_combobox.getSelectedItem().toString()), row, 3);
                model.setValueAt(loaiSP_combobox.getSelectedItem().toString(), row, 4);
                model.setValueAt(xuatxu_combobox.getSelectedItem().toString(), row, 5);
                model.setValueAt(giaNhap_text.getText(), row, 6);
                model.setValueAt(giaBan_text.getText(), row, 7);
                model.setValueAt(daBan_text.getText(), row, 8);
                model.setValueAt(dangBan_text.getText(), row, 9);
            } else {
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin không thành công","Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }                                             

    private void showAll_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        showAll();
        tongSP_text.setText(String.valueOf(model.getRowCount()));
    }                                              

   //Sự kiện tìm kiếm năm sản xuất của các sản phẩm
    private void searchYear_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban from Information where namSX = " + Integer.parseInt(namSX_combobox.getSelectedItem().toString()));
            model = (DefaultTableModel) jTable1.getModel();
            while (rs.next()){
                idSP = rs.getString("ID");
                nameSP = rs.getString("nameSP");
                nhaSX = rs.getString("nhaSX");
                namSX = String.valueOf(rs.getInt("namSX"));
                loaiSP = rs.getString("loaiSP");
                xuatxu = rs.getString("xuatxu");
                gianhapkho = String.valueOf(rs.getInt("gianhapkho"));
                giaban = String.valueOf(rs.getInt("giaban"));
                daban = String.valueOf(rs.getInt("daban"));
                dangban = String.valueOf(rs.getInt("dangban"));
                //Thêm dấu chấm vào trong giá tiền trước khi xuất ra bảng dữ liệu
                StringBuilder str1 = new StringBuilder(gianhapkho);
                str1 = addDot(gianhapkho);
                StringBuilder str2 = new StringBuilder(giaban);
                str2 = addDot(giaban);
                //Đưa dữ liệu từ CSDL vào trong Table              
                Object []row = {idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,str1,str2,daban,dangban};
                model.addRow(row);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (jTable1.getRowCount() == 0 )
            JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sản phẩm", "Thông báo",JOptionPane.INFORMATION_MESSAGE);
    }                                                 
    //Sự kiện tìm kiếm loại sản phẩm
    private void searchType_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban from Information where loaiSP = N'" +loaiSP_combobox.getSelectedItem().toString() +"'" );
            model = (DefaultTableModel) jTable1.getModel();
            while (rs.next()){
                idSP = rs.getString("ID");
                nameSP = rs.getString("nameSP");
                nhaSX = rs.getString("nhaSX");
                namSX = String.valueOf(rs.getInt("namSX"));
                loaiSP = rs.getString("loaiSP");
                xuatxu = rs.getString("xuatxu");
                gianhapkho = String.valueOf(rs.getInt("gianhapkho"));
                giaban = String.valueOf(rs.getInt("giaban"));
                daban = String.valueOf(rs.getInt("daban"));
                dangban = String.valueOf(rs.getInt("dangban"));
                //Thêm dấu chấm vào trong giá tiền trước khi xuất ra bảng dữ liệu
                StringBuilder str1 = new StringBuilder(gianhapkho);
                str1 = addDot(gianhapkho);
                StringBuilder str2 = new StringBuilder(giaban);
                str2 = addDot(giaban);
                //Đưa dữ liệu từ CSDL vào trong Table              
                Object []row = {idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,str1,str2,daban,dangban};
                model.addRow(row);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (jTable1.getRowCount() == 0 )
            JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sản phẩm", "Thông báo",JOptionPane.INFORMATION_MESSAGE);
    }                                                 
    //Sự kiện tìm kiếm nguồn gốc của các loại sản phẩm
    private void searchOrigin_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        jTable1.setModel(new DefaultTableModel(null, new String[]{"Mã sản phẩm","Tên sản phẩm","Nhà SX","Năm SX","Loại","Xuất xứ","Giá nhập kho","Giá bán","Đã bán","Đang bán"}));
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(URL);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select ID,nameSP,nhaSX,namSX,loaiSP,xuatxu,gianhapkho,giaban,daban,dangban from Information where xuatxu = N'"+xuatxu_combobox.getSelectedItem().toString()+"'" );
            model = (DefaultTableModel) jTable1.getModel();
            while (rs.next()){
                idSP = rs.getString("ID");
                nameSP = rs.getString("nameSP");
                nhaSX = rs.getString("nhaSX");
                namSX = String.valueOf(rs.getInt("namSX"));
                loaiSP = rs.getString("loaiSP");
                xuatxu = rs.getString("xuatxu");
                gianhapkho = String.valueOf(rs.getInt("gianhapkho"));
                giaban = String.valueOf(rs.getInt("giaban"));
                daban = String.valueOf(rs.getInt("daban"));
                dangban = String.valueOf(rs.getInt("dangban"));
                //Thêm dấu chấm vào trong giá tiền trước khi xuất ra bảng dữ liệu
                StringBuilder str1 = new StringBuilder(gianhapkho);
                str1 = addDot(gianhapkho);
                StringBuilder str2 = new StringBuilder(giaban);
                str2 = addDot(giaban);
                //Đưa dữ liệu từ CSDL vào trong Table              
                Object []row = {idSP,nameSP,nhaSX,namSX,loaiSP,xuatxu,str1,str2,daban,dangban};
                model.addRow(row);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (jTable1.getRowCount() == 0 )
            JOptionPane.showMessageDialog(rootPane, "Không tìm thấy sản phẩm", "Thông báo",JOptionPane.INFORMATION_MESSAGE);
    }                                                   

    private void sort_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        //Tạo lựa chọn cho thuộc tính sản phẩm và cách sắp xếp
        String properties[] = {"Năm sản xuất","Giá nhập kho","Giá bán","Đã bán","Đang bán"};
        String type[] = {"Giảm dần","Tăng dần"};

        JComboBox cb1 = new JComboBox(properties);
        JComboBox cb2 = new JComboBox(type);
        //Thực hiện lựa chọn
        String prop;
        String tpe;

        int input1 = JOptionPane.showConfirmDialog(this, cb1, "Chọn thuộc tính sản phẩm", JOptionPane.DEFAULT_OPTION);
        int input2 = JOptionPane.showConfirmDialog(this, cb2, "Chọn cách sắp xếp", JOptionPane.DEFAULT_OPTION);

        if (input1 == JOptionPane.OK_OPTION && input2 == JOptionPane.OK_OPTION){
            //Lấy lựa chọn trong jCombobox
            prop = cb1.getSelectedItem().toString();
            tpe = cb2.getSelectedItem().toString();
            //Thực hiện sắp xếp
            getData(prop, tpe);
        }
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_QLSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_QLSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_QLSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_QLSP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_QLSP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton add_button;
    private javax.swing.JButton close_button;
    private javax.swing.JLabel currency1_label;
    private javax.swing.JLabel currency2_label;
    private javax.swing.JTextField daBan_text;
    private javax.swing.JLabel daban_label;
    private javax.swing.JTextField dangBan_text;
    private javax.swing.JLabel dangban_label;
    private javax.swing.JButton deleteAll_button;
    private javax.swing.JButton delete_button;
    private javax.swing.JPanel func_panel;
    private javax.swing.JTextField giaBan_text;
    private javax.swing.JTextField giaNhap_text;
    private javax.swing.JButton hide_button;
    private javax.swing.JLabel idSP_label;
    private javax.swing.JTextField idSP_text;
    private javax.swing.JLabel in4_label;
    private javax.swing.JPanel in4_panel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> loaiSP_combobox;
    private javax.swing.JLabel loai_label;
    private javax.swing.JComboBox<String> namSX_combobox;
    private javax.swing.JLabel namSX_label;
    private javax.swing.JLabel nameSP_label;
    private javax.swing.JTextField nameSP_text;
    private javax.swing.JLabel nhaSX_label;
    private javax.swing.JTextField nhaSX_text;
    private javax.swing.JButton reset_button;
    private javax.swing.JScrollPane scrollpane_table;
    private javax.swing.JButton searchOrigin_button;
    private javax.swing.JButton searchType_button;
    private javax.swing.JButton searchYear_button;
    private javax.swing.JButton showAll_button;
    private javax.swing.JButton sort_button;
    private javax.swing.JLabel tienban_label;
    private javax.swing.JLabel tiennhap_label;
    private javax.swing.JLabel title_label;
    private javax.swing.JPanel title_panel;
    private javax.swing.JLabel tongSP_label;
    private javax.swing.JTextField tongSP_text;
    private javax.swing.JButton update_button;
    private javax.swing.JComboBox<String> xuatxu_combobox;
    private javax.swing.JLabel xuatxu_label;
    // End of variables declaration                   
}
