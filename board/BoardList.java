/*게시물 목록 페이지*/

package day1111.board;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class BoardList extends JPanel{
	JTable table;
	JScrollPane scroll;
	JButton bt;
	BoardApp boardApp;
	BoardModel boardModel;
	Connection con;
	
	public BoardList(BoardApp boardApp) {
		this.boardApp = boardApp;
		boardApp.setBoardList(this);
		con = boardApp.getCon();
		table = new JTable(boardModel = new BoardModel());
		scroll = new JScrollPane(table);
		bt = new JButton("글 등록");
		setLayout(new BorderLayout());
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				BoardDetail boardDetail = (BoardDetail)boardApp.getPages(BoardApp.BOARD_DETAIL);
				int row = table.getSelectedRow();
				boardDetail.getDetail(Integer.parseInt(boardModel.data[row][0]));
				boardApp.setPage(boardApp.BOARD_DETAIL);//상세보기
			}
		});
		
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//글쓰기 폼의 작성자에 아이디 넣어두기
				BoardWrite page = (BoardWrite)boardApp.getPages(BoardApp.BOARD_WRITE);
				page.t_writer.setText(boardApp.getBoardMember().getM_id());;

				//글쓰기 폼 보여주기
				boardApp.setPage(boardApp.BOARD_WRITE);
			}
		});
		
		getList();
		
		add(scroll);
		add(bt, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(780, 500));
		setVisible(true);
		
	}
	
	//게시물 가져오기! (복습)
	//rs에 담겨진 데이터를 TableModel이 보유한 이차원배열 data에 대입!!
	public void getList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from board order by board_id desc";
		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);//쿼리준비, 읽기 전용(READ-ONLY)
																		//스크롤이 가능하고, 읽기 전용의 rs를 만들기 위한 옵션
			rs = pstmt.executeQuery();
			
			rs.next(); rs.last();//첫번째 레코드 부터 마지막 레코드로 jump
			int currentRow = rs.getRow();//커서를 제일 마지막으로 보내기
			rs.beforeFirst();//첫번째 레코드 보다도 이전으로 되돌림(즉 위치 초기화)
			
			String[][] data = new String[currentRow][boardModel.column.length];
			int index = 0;
			while(rs.next()) {
				String[] record = new String[boardModel.column.length];
				
				record[0] = Integer.toString(rs.getInt("board_id"));
				record[1] = rs.getString("title");
				record[2] = rs.getString("writer");
				record[3] = rs.getString("regdate");
				record[4]= Integer.toString(rs.getInt("hit"));
				
				data[index++] = record;
			}
			
			boardModel.data = data;
			
			table.updateUI();//jtable UI 갱신
			
			
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



















