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
			if (BallController.lastPlayer == 1)
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

		this.gameObject.GetComponent<Renderer> ().enabled = false;
		this.gameObject.GetComponent<Collider> ().enabled = false;

		int random = Random.Range (1, 6);

		if (random >= 1 && random <=3) {
			ball.GetComponent<Rigidbody> ().velocity = ball.GetComponent<Rigidbody> ().velocity * 1.5f;
			yield return new WaitForSeconds (3);
			ball.GetComponent<Rigidbody> ().velocity = ball.GetComponent<Rigidbody> ().velocity / 1.5f;
		} else if (random == 4) {
			print ("rotate time");
			if (!rotate) {
				rotate = true;
				yield return new WaitForSeconds (4);
				Camera.main.transform.rotation = Quaternion.Euler (new Vector3 (90, 270, 0));
				rotate = false;
			}
		} else if (random == 5) {
			GameObject.Find ("Directional Light").GetComponent<Light>().enabled = false;
			yield return new WaitForSeconds (1f);
			GameObject.Find ("Directional Light").GetComponent<Light>().enabled = true;
		}

		Destroy (this.gameObject);
	}

	void FixedUpdate() {
		float degreesPerSecond = 30.0f;

		if (rotate == true)
		if (Camera.main.transform.rotation != Quaternion.Euler (new Vector3 (90, 90, 0))) {
			Camera.main.transform.Rotate (new Vector3 (0, 0, 1) * degreesPerSecond * Time.deltaTime);
		} 
	}

}
