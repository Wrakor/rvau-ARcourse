using UnityEngine;
using System.Collections;

public class BallController : MonoBehaviour {

	private Rigidbody rb;
	public float speed;
	private static Vector3 initialposition;
	public static int lastPlayer;

	void Start(){
		lastPlayer = 1;
		initialposition = transform.position;

		rb = GetComponent<Rigidbody> ();
		rb.velocity = new Vector3 (0, 0, speed);
	}

	void Update() {
		if (this.transform.position.x > 50 || this.transform.position.x < -50 || this.transform.position.y > 50 || this.transform.position.y < -50) {
			this.transform.position = initialposition;
			print ("fodeu");
		}

	}

	// Aplicar força na bola conforme o movimento do pad do jogador
	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("Player")) {

			if (collisionInfo.gameObject.name == "Player1") {
				lastPlayer = 1;
				if (Input.GetKey (KeyCode.W))
					this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (-speed, 0f, speed);
				else if (Input.GetKey (KeyCode.S))
					this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (speed, 0f, speed);
			}
			else {
				lastPlayer = 2;
				if (Input.GetKey (KeyCode.UpArrow))
					this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (-speed, 0f, -speed);
				else if (Input.GetKey (KeyCode.DownArrow))
					this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (speed, 0f, -speed);
			}
		} 
		else if (collisionInfo.gameObject.CompareTag ("EndWall")) {
			StartCoroutine(newBall(collisionInfo.gameObject));
		}
	}
	
	IEnumerator newBall(GameObject go) {
		//Desativar a bola, atribuir pontos, escolher nova posicao de inicio da bola e reativa-la
		this.gameObject.GetComponent<MeshRenderer> ().enabled = false;
		this.gameObject.GetComponent<Collider> ().enabled = false;
		yield return new WaitForSeconds (1.5f);

		speed = 19;
		if (go.name == "SouthWall") { //pontos para o p2 
			GameObject.Find ("Player2").GetComponent<PlayerController> ().incrementScore(3);

			float player2X = GameObject.Find("Player2").transform.position.x;
			this.transform.position = new Vector3 (player2X, initialposition.y, 20);
			rb.velocity = new Vector3 (0, 0, -speed);

		} else { //pontos para o p1
			GameObject.Find ("Player1").GetComponent<PlayerController>().incrementScore(3);

			float player1X = GameObject.Find("Player1").transform.position.x;
			this.transform.position = new Vector3(player1X, initialposition.y, -20);
			rb.velocity = new Vector3 (0, 0, speed);
		}

		this.gameObject.GetComponent<MeshRenderer> ().enabled = true;
		this.gameObject.GetComponent<Collider> ().enabled = true;
	}

	void playAgain() {
		Application.LoadLevel (Application.loadedLevel);		
	}
} 