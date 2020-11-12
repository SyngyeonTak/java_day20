package day1111.board;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import day1111.member.Board;

public class BoardWrite extends JPanel{
	JTextField t_title;
	JTextField t_writer;
	JTextArea content;
	JScrollPane scroll;
	JButton bt_regist;
	JButton bt_list;
	
	BoardApp boardApp;
	Connection con;
	BoardList boardList;
	public BoardWrite(BoardApp boardApp) {
		this.boardApp = boardApp;
		this.con = boardApp.getCon();
		t_title = new JTextField();
		t_writer = new JTextField();
		content = new JTextArea();
		scroll = new JScrollPane(content);
		bt_regist = new JButton("글등록");
		bt_list = new JButton("목록보기");
		//스타일
		t_title.setPreferredSize(new Dimension(780, 35));
		t_writer.setPreferredSize(new Dimension(780, 35));
		scroll.setPreferredSize(new Dimension(780, 200));//스크롤에 크기 부여
		
		//조립
		add(t_title);
		add(t_writer);
		add(scroll);
		add(bt_regist);
		add(bt_list);
		
		//목록으로 가기 구현
		bt_list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardApp.setPage(boardApp.BOARD_LIST);
			}
		});
		
		bt_regist.addActionListener((e)->{
			regist();
			BoardList boardList = (BoardList)boardApp.getPages(BoardApp.BOARD_LIST);//목록보기 메서드 호출
			boardList.getList();
		});
		
		setPreferredSize(new Dimension(780, 500));
		setVisible(true);
	}
	
	//글 등록하기
	public void regist() {
		PreparedStatement pstmt = null;
		String sql = "insert into board(board_id, title, writer, content)";
		sql += " values(seq_board.nextval, ?, ?, ?)";
		
		try {
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, t_title.getText());
			pstmt.setString(2, t_writer.getText());//BoardApp의 회원정보를 이용
			pstmt.setString(3, content.getText());
			
			int result = pstmt.executeUpdate();//쿼리 실행
			
			if(result != 1) {
				JOptionPane.showMessageDialog(this, "글쓰기 실패");
			}else {
				JOptionPane.showMessageDialog(this, "글쓰기 성공");
				boardApp.setPage(boardApp.BOARD_LIST);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}


















