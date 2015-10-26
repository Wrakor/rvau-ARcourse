using UnityEngine;
using System.Collections;

public class BlockController : MonoBehaviour {

	public GameObject miniball;

	void Start(){
		InitializeColor ();
	}

	public void InitializeColor(){
		for (int i = 0; i < this.transform.childCount; i++)
			GetComponentsInChildren<Renderer>()[i].material.color = new Color (Random.Range (0.0f, 1.0f), Random.Range (0.0f, 1.0f), Random.Range (0.0f, 1.0f));
	}

	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("Ball")) {
			//print ("aa" + miniball.transform.position.x);
			//
			GameObject miniball = (GameObject) Instantiate (GameObject.FindGameObjectWithTag("Miniball"), collisionInfo.transform.position, collisionInfo.transform.rotation);
			miniball.GetComponent<Rigidbody>().velocity = -collisionInfo.gameObject.GetComponent<Rigidbody>().velocity;
		}
	}
	 
}
