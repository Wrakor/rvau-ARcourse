using UnityEngine;
using System.Collections;

public class BallController : MonoBehaviour {

	private Rigidbody rb;
	public float speed;
	private Vector3 initialposition;

	void Start(){
		this.gameObject.GetComponent<MeshRenderer> ().enabled = true;
		initialposition = transform.position;
		rb = GetComponent<Rigidbody> ();

		if (transform.position.z > 0)
			rb.velocity = new Vector3 (0, 0, -speed);
		else
			rb.velocity = new Vector3 (0, 0, speed);
	}

	// Aplicar força na bola conforme o movimento do pad
	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("Player")) {

			if (Input.GetKey (KeyCode.UpArrow))
				this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (-speed, 0f, speed);
			else if (Input.GetKey (KeyCode.DownArrow))
				this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (speed, 0f, speed);
			else if (Input.GetKey (KeyCode.W))
				this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (-speed, 0f, -speed);
			else if (Input.GetKey (KeyCode.S))
				this.gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (speed, 0f, -speed);

		} 
		else if (collisionInfo.gameObject.CompareTag ("EndWall")) {
			StartCoroutine(newBall(collisionInfo.gameObject));
		}
	}

	IEnumerator newBall(GameObject go) {
		rb.velocity = new Vector3 (0, 0, 0);
		this.gameObject.GetComponent<MeshRenderer> ().enabled = false;
		yield return new WaitForSeconds (1.5f);

		if (go.name == "SouthWall") { //pontos para o p2 
			Instantiate (this.gameObject, new Vector3 (initialposition.x, initialposition.y, 15), transform.rotation);
			GameObject.Find ("Player2").GetComponent<PlayerController> ().incrementScore(3);
		} else {
			Instantiate (this.gameObject, new Vector3 (transform.position.x, transform.position.y, -15), transform.rotation);
			GameObject.Find ("Player1").GetComponent<PlayerController>().incrementScore(3);
		}

		Destroy(this.gameObject);
	}

	void OnCollisionExit(Collision collisionInfo) {
		//print ("No longer in contact with " + collisionInfo.transform.name);
		if (collisionInfo.collider.gameObject.CompareTag ("Block")) {
			Destroy( collisionInfo.collider.gameObject);			
		}
	}
} 