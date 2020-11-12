package day1111.board;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import day1111.member.Board;

public class BoardDetail extends JPanel{
	JTextField t_title;
	JTextField t_writer;
	JTextArea content;
	JScrollPane scroll;
	
	JButton bt_edit;//수정
	JButton bt_del;//삭제
	JButton bt_list;
	
	BoardApp boardApp;
	Connection con;
	Board board = null;//수정, 삭제시에도 이 안에 들어있는 정보들을 활용하기 위해
								//지역변수로 선언하지 않고, 멤버변수로 선언
	public BoardDetail(BoardApp boardApp) {
		
		this.boardApp = boardApp;
		con = boardApp.getCon();
		t_title = new JTextField();
		t_writer = new JTextField();
		content = new JTextArea();
		
		scroll = new JScrollPane(content);
		
		bt_edit = new JButton("수정하기");
		bt_del = new JButton("삭제하기");
		bt_list = new JButton("목록보기");
		
		//스타일
		t_title.setPreferredSize(new Dimension(780, 35));
		t_writer.setPreferredSize(new Dimension(780, 35));
		scroll.setPreferredSize(new Dimension(780, 200));//스크롤에 크기 부여
		
		//조립
		add(t_title);
		add(t_writer);
		add(scroll);
		add(bt_edit);
		add(bt_del);
		add(bt_list);
		
		bt_list.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boardApp.setPage(boardApp.BOARD_LIST);//목록보기
			}
		});
		setPreferredSize(new Dimension(780, 500));
		setVisible(true);
	}
	
	//한 건 가져오기!!
	public void getDetail(int board_id) {
		
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		
		String sql = "select * from board where board_id = "+board_id;
		
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {//레코드가 있다면...
				board = new Board();//create empty object
				board.setBoard_id(rs.getInt("board_id"));
				board.setTitle(rs.getString("title"));
				board.setWriter(rs.getString("writer"));
				board.setRegdate(rs.getString("regdate"));
				board.setContent(rs.getString("content"));
				board.setHit(rs.getInt("hit"));
				
				//데이터 채우기!!
				t_title.setText(board.getTitle());//제목
				t_writer.setText(board.getWriter());//작성자
				content.setText(board.getContent());//상세보기
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}



































