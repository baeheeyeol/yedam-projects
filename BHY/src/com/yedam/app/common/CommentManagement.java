package com.yedam.app.common;

import java.util.List;
import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.Comment;
import com.yedam.app.board.CommentDAO;
import com.yedam.app.member.Member;

public class CommentManagement extends BoardManagement {


	public CommentManagement() {
	};
	
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	CommentDAO cDAO = CommentDAO.getInstance();

	public void CommentManagementRun(Member member, Board board) throws InterruptedException {
		while (true) {
			clear();
			Read(board);
			menuPrint();

			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 입력
				insertComent(member, board);
			} else if (menuNo == 2) {
				// 수정
				updateComent(member,board);
			} else if (menuNo == 3) {
				// 삭제
				deleteComent(member,board);
			} else if (menuNo == 9) {
				exit();
				break;
				// 뒤로가기
			} else {
				showInputError();
			}
		}
	}

	void deleteComent(Member member,Board board) throws InterruptedException {
		clear();
		Read(board);
		int commentNum = selectComment();
		if (checkComentNum(commentNum)) {
			if (checkMemberId(member)) {
				if (checkComentParent(commentNum)) {
					cDAO.update(commentNum);
				} else {
					cDAO.delete(commentNum);
				}
			}
		}
	}

	boolean checkComentParent(int commentNum) {
		List<Comment> list = cDAO.selectAll();
		for (Comment comment : list) {
			if (commentNum == comment.getCommentNumParent()) {
				return true;
			}
		}
		return false;
	}

	protected void updateComent(Member member,Board board) throws InterruptedException {
		clear();
		Read(board);
		int commentNum = selectComment();
		if (checkComentNum(commentNum)) {
			if (checkMemberId(member)) {
				cDAO.update(commentNum, inputContent(board));
			}
		}
	}

	// 등록
	protected void insertComent(Member member, Board board) throws InterruptedException {
		clear();
		Read(board);
		System.out.println("--------------------------------------------------");
		System.out.println("1.댓글 2.대댓글");
		System.out.println("--------------------------------------------------");
		System.out.print("번호>");
		int num = menuSelect();
		if (num == 1) {
			// 댓글 작성
			cDAO.insert(board.getBoardNum(), member, inputContent(board));
		} else if (num == 2) {
			// 대댓글 작성
			int commentNum = selectComment();
			if (checkComentNum(commentNum)) {
				cDAO.insertToInsert(board.getBoardNum(), member, inputContent(board), commentNum);
			}
		}
	}

	int selectComment() throws InterruptedException {
		int num = 0;
		try {
			System.out.print("댓글번호>");
			num = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
			Thread.sleep(1000);
		}
		return num;
	}

	String inputContent(Board board) {
		clear();
		Read(board);
		System.out.print("내용입력>");
		return sc.nextLine();
	}

	// 본인 댓글 여부 확인
	boolean checkMemberId(Member member) throws InterruptedException {
		List<Comment> list = cDAO.selectAll();
		for (Comment comment : list) {
			System.out.println("1");
			System.out.println( comment.getMemberId());
			System.out.println("2");
			System.out.println(member.getMemberId());
			if (comment.getMemberId().equals(member.getMemberId())) {
				return true;
			}
		}
		System.out.println("본인의 댓글이 아닙니다.");
		Thread.sleep(1000);
		return false;
	}

	// 댓글 존재 확인
	boolean checkComentNum(int commentNum) throws InterruptedException {
		List<Comment> list = cDAO.selectAll();
		for (Comment comment : list) {
			if (comment.getCommentNum() == commentNum) {
				return true;
			}
		}
		System.out.println("선택한 번호의 댓글이 존재하지 않습니다.");
		Thread.sleep(1000);
		return false;
	}

	void ComentUpdate(Member member, int boardNum) {
		cDAO.insert(boardNum, member, insertContent());
	}

	protected String insertContent() {
		System.out.println("내용을 입력하세요");
		return sc.nextLine();
	}

	protected void menuPrint() {
		System.out.println("1.댓글작성\t2.댓글수정\t3.댓글삭제\t9.뒤로가기");
		System.out.print("번호>");
	}

	protected int menuSelect() throws InterruptedException {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.err.println("--------------------------------------------------");
			System.err.println("숫자를 입력해주시기 바랍니다.");
			System.err.println("--------------------------------------------------");
			Thread.sleep(1000);
		}
		return menu;
	}

	protected void exit() throws InterruptedException {
		System.out.println("뒤로가기");
		Thread.sleep(1000);
	}

	protected void showInputError() throws InterruptedException {
		System.err.println("--------------------------------------------------");
		System.err.println("메뉴에서 입력해주시기 바랍니다.");
		System.err.println("--------------------------------------------------");
		Thread.sleep(1000);
	}

	public void clear() {
		for (int i = 0; i < 57; ++i)
			System.out.println();
	}
}
