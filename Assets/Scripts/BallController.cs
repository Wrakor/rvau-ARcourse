using UnityEngine;
using System.Collections;

public class BallController : MonoBehaviour {

	private Rigidbody rb;
	public float speed;
	private Vector3 initialposition;

	void Start(){
		initialposition = transform.position;
		rb = GetComponent<Rigidbody> ();
		rb.velocity = new Vector3 (0, 0, speed);
	}

	// Aplicar força na bola conforme o movimento do pad
	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("Pad")) {

			if (Input.GetKey (KeyCode.UpArrow))
				this.gameObject.GetComponent<Rigidbody>().velocity = new Vector3(-speed, 0f, speed);
			else if (Input.GetKey (KeyCode.DownArrow))
				this.gameObject.GetComponent<Rigidbody>().velocity = new Vector3(speed, 0f, speed);
		}
	}

	void OnCollisionExit(Collision collisionInfo) {
		//print ("No longer in contact with " + collisionInfo.transform.name);
		if (collisionInfo.collider.gameObject.CompareTag ("Block")) {
			Destroy( collisionInfo.collider.gameObject);			
		}
	}
} 