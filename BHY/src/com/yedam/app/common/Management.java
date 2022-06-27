package com.yedam.app.common;

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

	public Management() {
	}

	public void ManagementRun(Member member) throws InterruptedException {
		while (true) {
			clear();
			readNotice();
			readBoard(member);
			menuPrint(member);
			int menuNo = menuSelect();
			if (menuNo == 1) {
				// 작성
				boardWrtie(member);
			} else if (menuNo == 2) {
				// 선택
				boardChoiceType(member);
			} else if (menuNo == 3 && member.getRole() != 3) {
				// 내 정보
				new MyInfoManagement(member);
			} else if (menuNo == 9) {
				// 프로그램 종료
				exit();
				break;
			} else {
				showInputError();
			}
		}
	}

	void menuPrint(Member member) {
		String str = "1.작성 2.선택";
		if (member.getRole() != 3) {
			str += " 3.내 정보";
		}
		str += " 9.프로그램종료";
		System.out.println(str);
		System.out.print("번호>");
	}

	void boardChoiceType(Member member) throws InterruptedException {
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
				break;
			}
	}

	protected void selectBoard(Member member) throws InterruptedException {
		while (true) {
			readBoardMy(member);
			System.out.println("==================");
			System.out.printf("선택할 게시판번호 입력>");
			int boardNum = menuSelect();
			if (existBoard(boardNum)) {
				Board board = bDAO.selectOne(boardNum);
				//new BoardManagement().BoardManagementRun(board, member);
			new BoardManagement().BoardManagementRun(board, member);
				return;
			} else {
				System.out.println("입력한 번호의 게시판이 없습니다.");
				Thread.sleep(1000);
			}
		}

	}

	protected void selectBoard(Member member, int boardType) throws InterruptedException {
		if (boardType == 1) {
			clear();
			readNotice();
			System.out.print("선택할 게시판번호 입력>");
			int boardNum = menuSelect();
			if (existNoticeBoard(boardNum)) {
				NoticeBoard ntboard = ntDAO.selectOneTitle(boardNum);
				new NoticeManagement(ntboard, member);
				return;
			} else {
				System.out.println("입력한 번호의 게시판이 없습니다.");
				Thread.sleep(1000);
			}
			return;
		} else if (boardType == 2) {
			clear();
			readBoard(member);
			System.out.printf("선택할 게시판번호 입력>");
			int boardNum = menuSelect();
			if (existBoard(boardNum)) {
				Board board = bDAO.selectOne(boardNum);
				new BoardManagement().BoardManagementRun(board, member);
				return;
			} else {
				System.out.println("입력한 번호의 게시판이 없습니다.");
				Thread.sleep(1000);
			}
			return;
		} else {
			System.out.println("다시 입력하세요.");
			Thread.sleep(1000);
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

	protected int selectBoardType() throws InterruptedException {
		System.out.println("1.공지 2.자유 9.뒤로가기");
		System.out.printf("게시판종류선택>");
		int num = menuSelect();
		return num;
	};

	protected void readNotice() {
		List<NoticeBoard> list = ntDAO.selelctAll();
		System.out.println("-=+-=+-=+공지+=-+=-+=-+=-");
		for (NoticeBoard Ntboard : list) {
			System.out.println(Ntboard.getBoardNum() + ".\t" + Ntboard.getBoardTitle());
		}
		System.out.println("=*=*=*=*=*=*=*=*=*=*=*=*");
	}

	protected void readBoard(Member member) {
		List<Board> list = bDAO.selelctAll();
		System.out.println("*********게시판**********");
		for (Board board : list) {
			if (member.getRole() == 0) {
				String str = board.getBoardNum() + ".\t" + board.getBoardTitle() + "\t\t작성자 : " + board.getMemberId();

				if (board.getBoardInvisible() == 1) {
					str += "/ 익명여부 : o";
				} else {
					str += " /익명여부 : x";
				}
				System.out.println(str);
			} else if (board.getBoardInvisible() == 1 && !member.getMemberId().equals(board.getMemberId())) {
				System.out.println(board.getBoardNum() + ".\t" + board.getBoardTitle() + "\t\t작성자 : " + "****");
			} else {
				System.out
						.println(board.getBoardNum() + ".\t" + board.getBoardTitle() + "\t\t작성자 : " + board.getMemberId());
			}
		}
		System.out.println("************************");
	}

	protected void readBoardMy(Member member) {
		List<Board> list = bDAO.selectAll(member.getMemberId());
		System.out.println("*********게시판**********");
		for (Board board : list) {
			System.out.println(board.getBoardNum() + ".\t\t" + board.getBoardTitle());
		}
	}

	protected void boardWrtie(Member member) throws InterruptedException {
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

	boolean anonymity() throws InterruptedException {
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
		NoticeBoard noticeBoard2 = NoticeBoardinputContent(boardNum, member);
		ntDAO.insertcontent(noticeBoard2);
	}

	void inputBoard(Member member) throws InterruptedException {
		Board board = BoardinputTitle(member);
		bDAO.insert(board);
		System.out.println("====================");
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
		System.out.print("제목>");
		noticeBoard.setBoardTitle(sc.nextLine());
		noticeBoard.setMemberId(member.getMemberId());
		return noticeBoard;
	}

	protected Board BoardinputContent(int boardNum, Member member) throws InterruptedException {
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
		System.out.print("제목>");
		board.setBoardTitle(sc.nextLine());
		board.setMemberId(member.getMemberId());
		return board;
	}

	String inputContent(Member member) {
		System.out.println("작성 완료시 \'저장\' 입력");
		System.out.printf("내용>");
		String content = sc.nextLine();
		String temp = "";
		while (!content.isEmpty()) {
			temp = sc.nextLine();
			if (temp.equals("저장")) {
				break;
			}
			content += "\n" + temp;
		}
		return content;
	}

	protected int menuSelect() throws InterruptedException {
		int menu = 0;
		try {
			menu = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주시기 바랍니다.");
				Thread.sleep(1000);
		}
		return menu;
	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

	protected void showInputError() throws InterruptedException {
		System.out.println("메뉴에서 입력해주시기 바랍니다.");
			Thread.sleep(1000);
	}
	public void clear() 
	{
		for (int i = 0; i < 45; ++i) System.out.println();
	}
}
