package com.yedam.app.common;

import java.util.ArrayList;
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
	int currentPage = 1;
	boolean back;

	public Management() {
	}

	public void ManagementRun(Member member) throws InterruptedException {

		while (true) {
			clear();
			System.out.println("접속자 : " + member.getMemberId());
			readNotice();
			// readBoard(member);
			readBoardMain(member);
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

	protected void selectMyBoard(Member member) throws InterruptedException {
		while (true) {
			readBoardMy(member);
			if (back == true) {
				return;
			}
			System.out.println("=======================================================");
			System.out.println("선택할 게시판 ");
			int boardNum = menuSelect();
			if (existBoard(boardNum)) {
				Board board = bDAO.selectOne(boardNum);
				// new BoardManagement().BoardManagementRun(board, member);
				new BoardManagement().BoardManagementRun(board, member);
				return;
			} else {
				System.err.println("========================");
				System.out.println("입력한 번호의 게시판이 없습니다.");
				System.err.println("========================");
				Thread.sleep(1000);
			}
		}

	}

	protected void selectBoard(Member member, int boardType) throws InterruptedException {
		if (boardType == 1) {
			while (true) {
				clear();
				readNotice();
				System.out.print("선택할 게시판번호 입력>");
				int boardNum = menuSelect();
				if (existNoticeBoard(boardNum)) {
					NoticeBoard ntboard = ntDAO.selectOneTitle(boardNum);
					new NoticeManagement(ntboard, member);
					return;
				} else {
					System.err.println("========================");
					System.err.println("입력한 번호의 게시판이 없습니다.");
					System.err.println("========================");
					Thread.sleep(1000);
				}
			}
		} else if (boardType == 2) {
			clear();
			readBoard(member);
			if (this.back == true) {
				return;
			} else if (this.back == false) {
				System.out.printf("선택할 게시판번호 입력>");
				int boardNum = menuSelect();
				if (existBoard(boardNum)) {
					Board board = bDAO.selectOne(boardNum);
					new BoardManagement().BoardManagementRun(board, member);
					return;
				} else {
					System.err.println("========================");
					System.err.println("입력한 번호의 게시판이 없습니다.");
					System.err.println("========================");
					Thread.sleep(1000);
				}
				return;
			}
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
		System.out.println("=*=*=*=*=*=*=*=*=*=*=*= 공지 *=*=*=*=*=*=*=*=*=*=*=");
		for (NoticeBoard Ntboard : list) {
			System.out.println("No." + Ntboard.getBoardNum() + "\t" + Ntboard.getBoardTitle());
		}
		System.out.println("=*=*=*=*=*=*=*=*=*=*=*==*=*=*=*=*=*=*=*=*=*=*==*=*");
	}

	protected void readBoard(Member member, int currentPage) {
		List<Board> list = bDAO.selelctAll();
		int number = 10; // 페이지당 총 글 개수
		int totalNumber = list.size(); // 총 글 개수
		int totalPage = totalNumber / number + (totalNumber % number == 0 ? 0 : 1);// 총 페이지수
		int firstNumber = (currentPage - 1) * number; // 시작번호
		int lastNumber = firstNumber + number; // 마지막 번호
		if (currentPage == totalPage) {
			lastNumber = firstNumber + (totalNumber % number);
		}
		System.out.println("********************** 게시판 **********************");
		for (int i = firstNumber; i < lastNumber; i++) {
			if (member.getRole() == 0) {
				String str = "No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle() + "\t\t작성자 : "
						+ list.get(i).getMemberId();
				if (list.get(i).getBoardInvisible() == 1) {
					str += "/ 익명여부 : o";
				} else {
					str += " /익명여부 : x";
				}
				System.out.println(str);
			} else if (list.get(i).getBoardInvisible() == 1
					&& !member.getMemberId().equals(list.get(i).getMemberId())) {
				System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
						+ "\t\t작성자 : " + "****");
			} else {
				System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
						+ "\t\t작성자 : " + list.get(i).getMemberId());
			}
		}
		System.out.println("현재 페이지:" + currentPage);
		System.out.println("************************************************");
	}

	protected void readBoardMain(Member member) throws InterruptedException {
		List<Board> list = bDAO.selelctAll();
		int number = 10; // 페이지당 총 글 개수
		int totalNumber = list.size(); // 총 글 개수
		int currentPage = 1; // 현재 페이지
		int totalPage = totalNumber / number + (totalNumber % number == 0 ? 0 : 1);// 총 페이지수
		int firstNumber = (currentPage - 1) * number; // 시작번호
		int lastNumber = firstNumber + number; // 마지막 번호
		if (currentPage == totalPage) {
			lastNumber = firstNumber + (totalNumber % number);
		}
		System.out.println("********************** 게시판 **********************");
		for (int i = firstNumber; i < lastNumber; i++) {
			if (member.getRole() == 0) {
				String str = "No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle() + "\t\t작성자 : "
						+ list.get(i).getMemberId();
				if (list.get(i).getBoardInvisible() == 1) {
					str += "/ 익명여부 : o";
				} else {
					str += " /익명여부 : x";
				}
				System.out.println(str);
			} else if (list.get(i).getBoardInvisible() == 1
					&& !member.getMemberId().equals(list.get(i).getMemberId())) {
				System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
						+ "\t\t작성자 : " + "****");
			} else {
				System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
						+ "\t\t작성자 : " + list.get(i).getMemberId());
			}
		}
		System.out.println("현재 페이지:" + currentPage);
		System.out.println("************************************************");
	}

	protected void readBoard(Member member) throws InterruptedException {
		List<Board> list = bDAO.selelctAll();
		int number = 10; // 페이지당 총 글 개수
		int totalNumber = list.size(); // 총 글 개수
		int currentPage = 1; // 현재 페이지
		int firstNumber = (currentPage - 1) * number; // 시작번호
		int totalPage = totalNumber / number + (totalNumber % number == 0 ? 0 : 1);// 총 페이지수
		int lastNumber = firstNumber + number; // 마지막 번호
		int rangePage = 5; // 표시될 페이지 범위
		int firstPage = 1; // 첫 번째 페이지
		int lastPage; // 마지막 페이지 개수
		List<Integer> page = new ArrayList<Integer>();
		for (int i = 1; i < totalPage; i++) {
			page.add(i);
		}

		while (true) {
			String pageNum = "";
			clear();
			readNotice();
			firstNumber = (currentPage - 1) * number;
			lastNumber = firstNumber + number;
			lastPage = firstPage + rangePage - 1;
			if (currentPage == totalPage) {
				lastNumber = firstNumber + (totalNumber % number);
			}
			if (lastPage > totalPage) {
				lastPage = totalPage;
			}

			System.out.println("********************** 게시판 **********************");
			for (int i = firstNumber; i < lastNumber; i++) {
				if (member.getRole() == 0) {
					String str = "No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle() + "\t\t작성자 : "
							+ list.get(i).getMemberId();
					if (list.get(i).getBoardInvisible() == 1) {
						str += "/ 익명여부 : o";
					} else {
						str += " /익명여부 : x";
					}
					System.out.println(str);
				} else if (list.get(i).getBoardInvisible() == 1
						&& !member.getMemberId().equals(list.get(i).getMemberId())) {
					System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
							+ "\t\t작성자 : " + "****");
				} else {
					System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
							+ "\t\t작성자 : " + list.get(i).getMemberId());
				}
			}
			System.out.println("************************************************");

			if (firstPage > 1) {
				pageNum += "<< ";
			}
			for (int i = firstPage; i <= lastPage; i++) {
				if (currentPage == i) {
					pageNum += "[" + i + "] ";
				} else if (currentPage != i) {
					pageNum += i + " ";
				}
			}
			if (firstPage + rangePage <= totalPage) {
				pageNum += ">>";
			}
			System.out.println(pageNum);
			System.out.println("************************************************");
			if (currentPage == 1 && totalPage > 1) {
				System.out.println("2.다음 3.게시글 선택 4.페이지 선택 9.뒤로가기");
			} else if (1 < currentPage && currentPage < totalPage && totalPage > 1) {
				System.out.println("1.이전 2.다음 3.게시글 선택 4.페이지 선택 9.뒤로가기");
			} else if (currentPage == totalPage && totalPage != 1) {
				System.out.println("1.이전 3.게시글 선택 4.페이지 선택9.뒤로가기");
			} else if (totalPage == 1) {
				System.out.println("3.선택 9.뒤로가기");
			}
			System.out.print("번호>");
			try {
				int num = Integer.parseInt(sc.nextLine());
				if (num == 1 && (1 < currentPage && currentPage < totalPage
						|| (currentPage == totalPage && totalPage > 1))) {
					currentPage--;
					if (currentPage < firstPage) {
						firstPage -= rangePage;
					}
				} else if (num == 2
						&& (1 < currentPage && currentPage < totalPage || (currentPage == 1 && totalPage > 1))) {
					currentPage++;
					if (currentPage > lastPage) {
						firstPage = lastPage + 1;
					}
				} else if (num == 3) {
					break;
				} else if (num == 4) {
					while (true) {
						System.out.print("페이지 번호>");
						currentPage = menuSelect();
						if (currentPage < firstPage || currentPage > lastPage) {
							System.err.println("다시 선택하세요");
							Thread.sleep(1000);
						} else {
							break;
						}
					}
				} else if (num == 9) {
					this.back = true;
					break;
				} else {
					System.err.println("메뉴에서 입력해주시기 바랍니다.");
					Thread.sleep(1000);
				}
			} catch (NumberFormatException e) {
				System.err.println("숫자를 입력해주시기 바랍니다.");
				Thread.sleep(1000);
			}
		}
	}

	protected void readBoardMy(Member member) throws InterruptedException {
		List<Board> list = bDAO.selectAll(member.getMemberId());
		int number = 10; // 페이지당 총 글 개수
		int totalNumber = list.size(); // 총 글 개수
		int currentPage = 1; // 현재 페이지
		int firstNumber = (currentPage - 1) * number; // 시작번호
		int totalPage = totalNumber / number + (totalNumber % number == 0 ? 0 : 1);// 총 페이지수
		int lastNumber = firstNumber + number; // 마지막 번호
		while (true) {
			firstNumber = (currentPage - 1) * number;
			lastNumber = firstNumber + number;
			if (currentPage == totalPage) {
				lastNumber = firstNumber + (totalNumber % number);
			}
			System.out.println("***********************게시판***********************");
			for (int i = firstNumber; i < lastNumber; i++) {
				System.out.println("No." + list.get(i).getBoardNum() + ".\t" + list.get(i).getBoardTitle()
						+ "\t\t작성자 : " + list.get(i).getMemberId());
			}
			System.out.println("현재 페이지:" + currentPage);
			System.out.println("************************************************");

			if (currentPage == 1 && totalPage > 1) {
				System.out.println("2.다음 3.선택 9.뒤로가기");
			} else if (1 < currentPage && currentPage < totalPage && totalPage > 1) {
				System.out.println("1.이전 2.다음 3.선택 9.뒤로가기");
			} else if (currentPage == totalPage && totalPage != 1) {
				System.out.println("1.이전 3.선택 9.뒤로가기");
			} else if (totalPage == 1) {
				System.out.println("3.선택 9.뒤로가기");
			}

			try {
				int num = Integer.parseInt(sc.nextLine());
				if (num == 1 && (1 < currentPage && currentPage < totalPage
						|| (currentPage == totalPage && totalPage > 1))) {
					currentPage--;
				} else if (num == 2
						&& (1 < currentPage && currentPage < totalPage || (currentPage == 1 && totalPage > 1))) {
					currentPage++;
				} else if (num == 3) {
					break;
				} else if (num == 9) {
					back = true;
					return;
				} else {
					System.err.println("메뉴에서 입력해주시기 바랍니다.");
					Thread.sleep(1000);
				}
			} catch (NumberFormatException e) {
				System.err.println("숫자를 입력해주시기 바랍니다.");
				Thread.sleep(1000);
			}
		}

	}

	protected void boardWrtie(Member member) throws InterruptedException {
		if (member.getRole() == 0) {
			clear();
			System.out.println("--------------------------------------------------");
			System.out.println("1.공지 2.자유");
			System.out.println("--------------------------------------------------");
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
			System.out.println("--------------------------------------------------");
			System.out.println("익명으로 하시겠습니까?");
			System.out.println("1.네 2.아니요");
			System.out.println("--------------------------------------------------");
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
		clear();
		NoticeBoard noticeBoard = NoticeBoardinputTitle(member);
		ntDAO.insert(noticeBoard);
		int boardNum = ntDAO.seq();
		NoticeBoard noticeBoard2 = NoticeBoardinputContent(boardNum, member);
		ntDAO.insertcontent(noticeBoard2);
	}

	void inputBoard(Member member) throws InterruptedException {
		clear();
		Board board = BoardinputTitle(member);
		bDAO.insert(board);
		System.out.println("==================================================");
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
		System.out.println("==================================================");
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
		System.out.println("==================================================");
		System.out.println("작성 완료시 \'저장\' 입력");
		System.out.println("==================================================");
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
			System.err.println("--------------------------------------------------");
			System.err.println("숫자를 입력해주시기 바랍니다.");
			System.err.println("--------------------------------------------------");
			Thread.sleep(1000);
		}
		return menu;
	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
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
