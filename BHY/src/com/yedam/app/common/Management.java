package com.yedam.app.common;

import java.io.Console;
import java.util.List;

import java.util.Scanner;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.NoticeBoard;
import com.yedam.app.board.NoticeBoardDAO;
import com.yedam.app.member.Member;

public class Management {
	Scanner sc = new Scanner(System.in);
	BoardDAO bDAO = BoardDAO.getInstance();
	NoticeBoardDAO ntDAO = NoticeBoardDAO.getInstance();

	public Management(Member member) {
		int role = member.getRole();
		while (true) {
			readNotice();
			readBoard(member);
			menuPrint();
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 작성
				boardWrtie(member);
			} else if (menuNo == 2) {
				// 선택
				boardChoiceType(member);
			} else if (menuNo == 3) {
				// 내 정보
			} else if (menuNo == 9) {
				// 프로그램 종료
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	void menuPrint() {
		System.out.println("1. 작성 2. 선택 3.내 정보 9.프로그램종료");
	}

	void boardChoiceType(Member member) {
		int boardType = selectBoardType();
		while (true)
			if (boardType == 1) {
				selectBoard(member, boardType);
				break;
			} else if (boardType == 2) {
				selectBoard(member, boardType);
				break;
			} else if (boardType == 9) {
				exit();
				break;
			} else {
				showInputError();
			}
		System.out.flush();

	}

	protected void selectBoard(Member member, int boardType) {
		if (boardType == 1) {
			readNotice();
			System.out.printf("선택할 게시판번호 입력>");
			int boardNum = menuSelect();
			if (existNoticeBoard(boardNum)) {
				NoticeBoard ntboard = ntDAO.selectOneTitle(boardNum);
				new NoticeManagement(ntboard, member);
				return;
			} else {
				System.out.println("입력한 번호의 게시판이 없습니다.");
			}
			return;
		} else if (boardType == 2) {
			readBoard(member);
			System.out.printf("선택할 게시판번호 입력>");
			int boardNum = menuSelect();
			if (existBoard(boardNum)) {
				Board board = bDAO.selectOne(boardNum);
				new BoardManagement(board, member);
				return;
			} else {
				System.out.println("입력한 번호의 게시판이 없습니다.");
			}
			return;
		} else {
			System.out.println("다시 입력하세요.");
		}
	}

	// 공지 게시판 존재유무
	private boolean existNoticeBoard(int boardNum) {
		List<Integer> list = ntDAO.selectAllBoardNum();
		for (int num : list) {
			if (num == boardNum) {
				return true;
			}
		}
		return false;
	}

	// 게시판 존재유무
	private boolean existBoard(int boardNum) {
		List<Integer> list = bDAO.selectAllBoardNum();
		for (int num : list) {
			if (num == boardNum) {
				return true;
			}
		}
		return false;
	}

	protected int selectBoardType() {
		System.out.println("1.공지 2.자유 9.뒤로가기");
		System.out.printf("게시판종류선택>");
		int num = menuSelect();
		return num;
	};

	protected void readNotice() {
		List<NoticeBoard> list = ntDAO.selelctAll();
		System.out.println("-=+-=+-=+공지+=-+=-+=-+=-");
		for (NoticeBoard Ntboard : list) {
			System.out.println(Ntboard.getBoardNum() + ". " + Ntboard.getBoardTitle());
		}
		System.out.println("=*=*=*=*=*=*=*=*=*=*=*=*");
	}

	protected void readBoard(Member member) {
		List<Board> list = bDAO.selelctAll();
		System.out.println("*********게시판**********");
		for (Board board : list) {
			if (member.getRole() == 0) {
				String str = board.getBoardNum() + ". " + board.getBoardTitle() + " 작성자 : " + board.getMemberId();
			
				if(board.getBoardInvisible()==1) 
				{
					str += "/ 익명여부 : o";
				}
				else 
				{
					str += " /익명여부 : x";
				}
			System.out.println(str);
			} else if (board.getBoardInvisible() == 1 && !member.getMemberId().equals(board.getMemberId())) {
				System.out.println(board.getBoardNum() + ". " + board.getBoardTitle() + " 작성자 : " + "****");
			} else {
				System.out
						.println(board.getBoardNum() + ". " + board.getBoardTitle() + " 작성자 : " + board.getMemberId());
			}
		}
		System.out.println("************************");
	}

	protected void boardWrtie(Member member) {
		if (member.getRole() == 0) {
			System.out.println("1.공지 2.자유");
			System.out.print("게시판 선택>");
			int num = menuSelect();
			if (num == 1) {
				inputNoticeBoard(member);
				return;
			} else if (num == 2) {
				inputBoard(member);
				return;
			}
		}
		inputBoard(member);
	}

	boolean anonymity() {
		while (true) {
			System.out.println("익명으로 하시겠습니까?");
			System.out.println("1.네 2.아니요");
			System.out.printf("선택>");
			int num = menuSelect();
			if (num == 1) {
				return true;
			} else if (num == 2) {
				return false;
			}
		}
	}

	void inputNoticeBoard(Member member) {
		NoticeBoard noticeBoard = NoticeBoardinputTitle(member);
		ntDAO.insert(noticeBoard);
		int boardNum = ntDAO.seq();
		System.out.println(boardNum);
		NoticeBoard noticeBoard2 = NoticeBoardinputContent(boardNum, member);
		System.out.println(noticeBoard2.getBoardNum());
		ntDAO.insertcontent(noticeBoard2);
	}

	void inputBoard(Member member) {
		Board board = BoardinputTitle(member);
		bDAO.insert(board);
		int boardNum = bDAO.seq();
		Board board2 = BoardinputContent(boardNum, member);
		bDAO.insertcontent(board2);
	}

	protected NoticeBoard NoticeBoardinputContent(int boardNum, Member member) {
		NoticeBoard noticeBoard = new NoticeBoard();
		noticeBoard.setBoardContent(inputContent(member));
		noticeBoard.setBoardNum(boardNum);
		return noticeBoard;
	}

	protected NoticeBoard NoticeBoardinputTitle(Member member) {
		NoticeBoard noticeBoard = new NoticeBoard();
		System.out.println("제목>");
		noticeBoard.setBoardTitle(sc.nextLine());
		noticeBoard.setMemberId(member.getMemberId());
		return noticeBoard;
	}

	protected Board BoardinputContent(int boardNum, Member member) {
		Board board = new Board();
		if (member.getRole() == 3) {
			board.setBoardContent(inputContent(member));
			board.setBoardInvisible(1);
			board.setBoardNum(boardNum);
		} else {
			board.setBoardContent(inputContent(member));
			if (anonymity()) {
				board.setBoardInvisible(1);
			}
			board.setBoardNum(boardNum);
		}
		return board;
	}

	protected Board BoardinputTitle(Member member) {
		Board board = new Board();
		System.out.println("제목>");
		board.setBoardTitle(sc.nextLine());
		board.setMemberId(member.getMemberId());
		return board;
	}

	String inputContent(Member member) {
		System.out.println("저장시 본인 비밀번호 입력");
		System.out.printf("내용>");
		String content = sc.nextLine();
		String temp = "";
		while (!content.isEmpty()) {
			temp = sc.nextLine();
			if (temp.equals(member.getMemberPwd())) {
				break;
			}
			content += "\n" + temp;
		}
		return content;
	}

	protected int menuSelect() {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
		}
		return menu;
	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
	}
}
