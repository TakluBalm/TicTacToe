import java.util.*;

class Player{
	TicTacToe t;	//	The TicTacToe object the player is playing with
	int sign;		//	The sign he/she has been alloted for playing
	int type;		//	Whether the player is a Human(0) or Computer(1)
	String name;	//	Name of the player

	private int games_won, games_lost, games_drawn;	//	Stats of the player
	private int last_play;							//	Last box the player played at

//	Construct for the class
	Player(TicTacToe _t, int _type, String _name){
		t = _t;
		type = _type;
		name = _name;
		last_play = -1;
		games_won = 0;
		games_drawn = 0;
		games_lost = 0;
	}

/************Stats update methods**************/
	void won(){
		games_won++;
	}
	void lost(){
		games_lost++;
	}
	void draw(){
		games_drawn++;
	}
	void printStats(){
		System.out.println(name + " has won: " + games_won);
		System.out.println(name + " has lost: " + games_lost);
		System.out.println(name + " has drawn: " + games_drawn);
	}

//	Make a move on the board
	void play(){
	//	If human ask the human where he/she wants to mark and mark there
		if(type == 0){
			Scanner scanner = new Scanner(System.in);
			System.out.print("Where do you want to play "+name+" [1-9]: ");
			int box = scanner.nextInt() - 1;
			while(box > 8 || box < 0 || !t.isEmpty(box)){
				if(box <= 8 && box >= 0){
					System.out.print("Place " + box + " has already been filled. Select an empty box: ");
					box = scanner.nextInt() - 1;
					continue;
				}
				System.out.print("Enter a valid value int the range [1-9]: ");
				box = scanner.nextInt() - 1;
			}
			last_play = box;
			t.mark(box, sign, this);
			return;
		}
	//	If Computer then analyze the board and make a suitable move according to it
		if(type == 1){
			int vertical;
			int horizontal;
			int diag;
			if(last_play != -1){
			// First see check if the player is able to win on this move
				vertical = CheckVertical(last_play);
				horizontal = CheckHorizontal(last_play);
				diag = CheckDiagonal(last_play);
				if(vertical != -1 && t.isEmpty(vertical)){
					t.mark(vertical, sign, this);
					last_play = vertical;
					System.out.println(name + " has marked the box number: "+ (last_play+1));
					return;
				}
				if(horizontal != -1 && t.isEmpty(horizontal)){
					t.mark(horizontal, sign, this);
					last_play = horizontal;
					System.out.println(name + " has marked the box number: "+ (last_play+1));
					return;
				}
				if(diag != -1 && t.isEmpty(diag)){
					t.mark(diag, sign, this);
					last_play = diag;
					System.out.println(name + " has marked the box number: "+ (last_play+1));
					return;
				}
			}
		//	Check if the other player can win on the next move and stop it
			vertical = CheckVertical(t.getLast());
			horizontal = CheckHorizontal(t.getLast());
			diag = CheckDiagonal(t.getLast());
			if(vertical != -1 && t.isEmpty(vertical)){
				t.mark(vertical, sign, this);
				last_play = vertical;
				System.out.println(name + " has marked the box number: "+ (last_play+1));
				return;
			}
			if(horizontal != -1 && t.isEmpty(horizontal)){
				t.mark(horizontal, sign, this);
				last_play = horizontal;
				System.out.println(name + " has marked the box number: "+ (last_play+1));
				return;
			}
			if(diag != -1 && t.isEmpty(diag)){
				t.mark(diag, sign, this);
				last_play = diag;
				System.out.println(name + " has marked the box number: "+ (last_play+1));
				return;
			}
		//	If even the opponent cant win on the next move then mark the bx where it would be optimal to do so
			if(t.isEmpty(4)){
				t.mark(4, sign, this);
				last_play = 4;
			}else{
				int a[];
				int b[];
				int corners[] = {0, 2, 6, 8};
				int middle[] = {1, 3, 5, 7};
				if(t.getValue(4) == sign){
					a = middle;
					b = corners;
				}
				else{
					a = corners;
					b = middle;
				}

				for(int i = 0; i < 4; i++){
					if(t.isEmpty(a[i])){
						t.mark(a[i], sign, this);
						last_play = a[i];
						System.out.println(name + " has marked the box number: "+ (last_play+1));
						return;
					}
				}
				for(int i = 0; i < 4; i++){
					if(t.isEmpty(b[i])){
						t.mark(b[i], sign, this);
						last_play = b[i];
						System.out.println(name + " has marked the box number: "+ (last_play+1));
						return;
					}
				}
			}
		}
	}

/**************Private methods**************/
	private int CheckVertical(int box){
		int box_abv = 3*((box/3 + 1)%3) + (box%3);
		int box_cur = 3*(box/3) + (box%3);
		int box_blw = 3*((box/3 + 2)%3) + (box%3);
		if(t.getValue(box_cur) == t.getValue(box_abv)){
			return box_blw;
		}
		if(t.getValue(box_cur) == t.getValue(box_blw)){
			return box_abv;
		}
		return -1;
	}
	private int CheckHorizontal(int box){
		int box_r = 3*(box/3) + ((box%3 + 1)%3);
		int box_cur = 3*(box/3) + (box%3);
		int box_l = 3*(box/3) + ((box%3 + 2)%3);
		if(t.getValue(box_cur) == t.getValue(box_r)){
			return box_l;
		}
		if(t.getValue(box_cur) == t.getValue(box_l)){
			return box_r;
		}
		return -1;
	}
	private int CheckDiagonal(int box){
		int cur = 3*(box/3) + (box%3);
		int cur_ld = 3*((box/3 + 1)%3) + ((box%3 + 2)%3);
		int cur_rd = 3*((box/3 + 1)%3) + ((box%3 + 1)%3);
		int cur_ru = 3*((box/3 + 2)%3) + ((box%3 + 1)%3);
		int cur_lu = 3*((box/3 + 2)%3) + ((box%3 + 2)%3);
		if(box == 2 || box == 4 || box == 6){
			if(t.getValue(cur) == t.getValue(cur_ld)){
				return cur_ru;
			}
			if(t.getValue(cur) == t.getValue(cur_ru)){
				return cur_ld;
			}
			return -1;
		}
		if(box == 0 || box == 4 || box == 8){
			if(t.getValue(cur) == t.getValue(cur_lu)){
				return cur_rd;
			}
			if(t.getValue(cur) == t.getValue(cur_rd)){
				return cur_lu;
			}
			return -1;
		}
		return -1;
	}
}

public class TicTacToe{
	private int[][] board = new int[3][3];
	private int last_box;					//	The index of recently marked box
	private Player last_player;				//	Points to the last player who takes a turn
	private int total_turns;				//	Total boxes marked
	private int players;					//	Number of players enrolled to play
	private int status;
/*
	Running:	0
	Won:		1
	Drawn:		-1
*/	
	Player p[] = new Player[2];	//	Array of players playing	

	//	Main Method
	public static void main(String[] args){

		TicTacToe t = new TicTacToe();		//	Instantiating TicTacToe object
		Scanner s = new Scanner(System.in);	//	Instatantiating scanner for input
		int mode;							//	Whether the player wants to play with another Human or Computer
		Player p1, p2;						//	Declaring 2 objects p1 and p2 for the 2 players that will interact with t(instance of TicTacToe)
		boolean playing = true;				//	Variable to know if the user wanted to keep playing or not

		//	Knowing the mode the user wants to play in
		while(true){
			System.out.print("Choose between\n1. Pass-n-Play\n2. v/s Computer\nEnter your choice: ");
			mode = s.nextInt();
			if(mode == 1 || mode == 2){
				mode--;
				break;
			}
		}

		//	Instantiating 2 players based on what mode the user selected
		if(mode == 0){
			System.out.print("\nEnter the name of player 1: ");
			p1 = new Player(t, 0, s.next());
			System.out.print("Enter the name of player 2: ");
			p2 = new Player(t, 0, s.next());
		}
		else{
			System.out.print("\nEnter your name: ");
			p1 = new Player(t, 0, s.next());
			p2 = new Player(t, 1, "Computer");
		}

		//	Playing the game as long as the user wants to keep playing
		do{
			t.Reset();						//	Reset the fields in the object to default values

			t.AddPlayer(p1);				//	Letting t know that p1 is the first player
			t.AddPlayer(p2);				//	Letting t know that p2 is the second player
		/*
			This is useful to check if a player does not move out of turn or to ensure that
			a third-player(other than the 2 playing) cannot interact with t
		*/

		//	Looping through two players taking turns till the game is over
			for(int i = 0; t.getStatus() == 0; i = (i == 0)?(1):0){
				t.print();
				if(i == 0){
					p1.play();
				}
				else{
					p2.play();
				}
			}
			t.print();

		//	Printing apporpriate output based on the outcome of the game
			if(t.status == 1){
				System.out.println(t.last_player.name + " has won!!");
				t.last_player.won();
				Player other;
				if(t.last_player == t.p[0])	other = t.p[1];
				else						other = t.p[0];
				System.out.println("Better luck next " + other.name + "!!");
				other.lost();
			}
			else{
				System.out.println("The game was drawn");
				t.p[0].draw();
				t.p[1].draw();
			}

		//	Asking the user if he/she wants to continue playing
			while(true){
				System.out.print("Do you want to play another?[yes/no]: ");
				if(s.hasNext()){
					String input = s.next();
					if(input.equals("yes") || input.equals("yema")){
						playing = true;
						break;
					}
					if(input.equals("no")){
						playing = false;
						break;
					}
				}
				s.nextLine();
				System.out.println("Enter valid input");
			}
		}while(playing);

	//	Print the overall statistics from all the rounds played
		t.p[0].printStats();
		t.p[1].printStats();
		s.close();
	}

//	The constructor for TicTacToe class
	TicTacToe(){
		status = 0;
		players = 0;
		last_player = null;
		last_box = -1;
	}

//	Mark a box with given sign
/*
	Sign policy:
		X = 1
		O = -1
		Empty = 0
*/
	void mark(int box, int sign, Player _p){
		int search;

		if(!isEmpty(box))	return;

		if(sign == 1)	search = 0;
		else if(sign == -1)	search = 1;
		else	return;
		
		if(_p != p[search])	return;

		board[box/3][box%3] = sign;
		last_box = box;
		last_player = _p;
		total_turns++;
		updateStatus();
	}

//	Check if the given box is empty
	boolean isEmpty(int box){
		if(box >= 0 && box < 9){
			if(board[box/3][box%3] == 0)	return true;
			return false;
		}
		return true;
	}

//	Add a player that can interact with the object
	void AddPlayer(Player _p){
		if(players < 2){
			_p.sign = assign();
			players++;
			p[players - 1] = _p;
		}
	}

//	Reset the object to default state
	void Reset(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				board[i][j] = 0;
			}
		}
		status = 0;
		players = 0;
		total_turns = 0;
	}

//	Print the current board state
	void print(){
		System.out.println("\t____________");
		for(int i = 0; i < 3; i++){
			System.out.print("\t");
			for(int j = 0; j < 3; j++){
				switch(board[i][j]){
					case 1:
						System.out.print(" X ");
						break;
					case -1:
						System.out.print(" O ");
						break;
					default:
						System.out.print(" "+(3*i + j + 1)+ " ");
				}
				if(j < 2)	System.out.print("|");
			}
			System.out.println();
			if(i < 2)	System.out.println("\t-----------");
		}
		System.out.println("\t____________");
	}

/****************Private methods********************/

//	Returns the sign that should be alloted the the next player
	private int assign(){
		switch(players){
			case 0: return 1;
			case 1: return -1;
			default: return 0;
		}
	}
//	Checks if the vertical line containing box is completed
	private boolean checkVertical(int box){
		int cur = board[box/3][box%3];
		int cur_blw = board[(box/3 + 1)%3][box%3];
		int cur_abv = board[(box/3 + 2)%3][box%3];
		if(cur == cur_abv && cur == cur_blw){
			return true;
		}
		return false;
	}
//	Checks if the horizontal line containing box is completed
	private boolean checkHorizontal(int box){
		int cur = board[box/3][box%3];
		int cur_r = board[box/3][(box%3 + 1)%3];
		int cur_l = board[box/3][(box%3 + 2)%3];
		if(cur == cur_r && cur == cur_l){
			return true;
		}
		return false;
	}
//	Checks if the diagonal containing box is completed
	private boolean checkDiagonal(int box){
		int cur = board[box/3][box%3];
		int cur_ld = board[(box/3 + 1)%3][(box%3 + 2)%3];
		int cur_rd = board[(box/3 + 1)%3][(box%3 + 1)%3];
		int cur_ru = board[(box/3 + 2)%3][(box%3 + 1)%3];
		int cur_lu = board[(box/3 + 2)%3][(box%3 + 2)%3];
		if(box == 2 || box == 4 || box == 6){
			if(cur == cur_ld && cur == cur_ru){
				return true;
			}
			return false;
		}
		if(box == 0 || box == 4 || box == 8){
			if(cur == cur_lu && cur == cur_rd){
				return true;
			}
			return false;
		}
		return false;
	}
//	Updates the status field of the object
	private void updateStatus(){
		if(checkVertical(last_box) || checkHorizontal(last_box) || checkDiagonal(last_box)){
			status = 1;
		}
		else if(total_turns == 9){
			status = -1;
		}
	}

/***************Get Methods************************/
	int getLast(){
		return last_box;
	}
	int getValue(int box){
		return board[box/3][box%3];
	}
	int getStatus(){
		return status;
	}
}