using UnityEngine;
using System.Collections;

public class BlockController : MonoBehaviour {

	public GameObject miniball;
	public static bool rotate = false, rotateLast = false;

	void Start(){
		InitializeColor ();
	}

	public void InitializeColor(){
			if (this.transform.name.Contains("Block"))
				GetComponent<Renderer>().material.color = new Color (Random.Range (0.0f, 1.0f), Random.Range (0.0f, 1.0f), Random.Range (0.0f, 1.0f));
	}

	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("Ball")) {
			GameObject miniball = (GameObject) Instantiate (GameObject.FindGameObjectWithTag("Miniball"), collisionInfo.transform.position, collisionInfo.transform.rotation);
			miniball.GetComponent<Rigidbody>().velocity = -collisionInfo.gameObject.GetComponent<Rigidbody>().velocity;
			if (collisionInfo.gameObject.GetComponent<BallController>().lastPlayer == 1)
				GameObject.Find ("Player1").GetComponent<PlayerController>().incrementScore(1);
			else
				GameObject.Find ("Player2").GetComponent<PlayerController>().incrementScore(1);

			if (this.transform.parent.name == "SpecialBlocks")
				StartCoroutine(PowerUp(collisionInfo.gameObject));
			else
				Destroy (this.gameObject);
		}
	}
	 
	IEnumerator PowerUp(GameObject ball) {
		int random = Random.Range (1, 4);

		if (random == 1)
			ball.GetComponent<Rigidbody> ().velocity = ball.GetComponent<Rigidbody> ().velocity / 1.5f;
		if (random == 2) {
			print ("rotate time");
			if (!rotate) {
				rotate = true;
				yield return new WaitForSeconds (3);
				Camera.main.transform.rotation = Quaternion.Euler (new Vector3(90, 270, 0));
				rotate = false;
			}
		}
	}

	void FixedUpdate() {
		float degreesPerSecond = 20.0f;

		if (rotate == true)
		if (Camera.main.transform.rotation != Quaternion.Euler (new Vector3 (90, 90, 0))) {
			Camera.main.transform.Rotate (new Vector3 (0, 0, 1) * degreesPerSecond * Time.deltaTime);
		} 
	}

}
