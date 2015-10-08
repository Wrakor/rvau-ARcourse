using UnityEngine;
using System.Collections;

public class PlayerController : MonoBehaviour {

	private Rigidbody rb;
	public float speed;
	private Vector3 initialposition;

	void Start(){
		initialposition = transform.position;
		rb = GetComponent<Rigidbody> ();
		rb.velocity = new Vector3 (0, 0, speed);
	}



	void OnCollisionExit(Collision collisionInfo) {
		print ("No longer in contact with " + collisionInfo.transform.name);
		if (collisionInfo.collider.gameObject.CompareTag ("Block")) {
			collisionInfo.collider.gameObject.SetActive (false);
			
		}
	}
} 