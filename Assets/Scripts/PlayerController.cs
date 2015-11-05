using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class PlayerController : MonoBehaviour {

	public float moveDistance;
	protected int score = 0;
	float topBorder = -12.5f, bottomBorder = 12.5f;

	public void incrementScore(int amount) {
		score += amount;

		if (this.gameObject.name == "Player1") {
			GameObject.Find("Player1Score").GetComponent<Text>().text = "Player 1: " + score;
		}
		else
			GameObject.Find("Player2Score").GetComponent<Text>().text = "Player 2: " + score;
	}
	
	// Update is called once per frame
	void Update() {
		/*
		transform.position = new Vector3 ((Input.mousePosition.x-(Screen.width/2))*0.1f, 0.5f, -15f);*/

		if (this.gameObject.name == "Player2") {

			if (Input.GetKey (KeyCode.UpArrow) && (transform.position.x - moveDistance > topBorder)) 
				transform.position = new Vector3 (transform.position.x - moveDistance, transform.position.y, transform.position.z);

			if (Input.GetKey (KeyCode.DownArrow) && transform.position.x + moveDistance < bottomBorder) 
				transform.position = new Vector3 (transform.position.x + moveDistance, transform.position.y, transform.position.z);
		} 
		else if (this.gameObject.name == "Player1") {
			if (Input.GetKey (KeyCode.W) && transform.position.x - moveDistance > topBorder) 
				transform.position = new Vector3 (transform.position.x - moveDistance, transform.position.y, transform.position.z);
			
			if (Input.GetKey (KeyCode.S) && transform.position.x + moveDistance < bottomBorder)
				transform.position = new Vector3 (transform.position.x + moveDistance, transform.position.y, transform.position.z);
		}
	}
}
