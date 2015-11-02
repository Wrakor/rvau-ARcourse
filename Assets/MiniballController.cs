using UnityEngine;
using System.Collections;

public class MiniballController : MonoBehaviour {

	void Start() {
		GameObject[] blocks = GameObject.FindGameObjectsWithTag ("Block");
		for (int i = 0; i < blocks.Length; i++)
			Physics.IgnoreCollision(this.gameObject.GetComponent<Collider>(), blocks[i].GetComponent<Collider>());
	}
	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("EndWall")) {
			Destroy(this.gameObject);
		}
		else if (collisionInfo.gameObject.CompareTag ("Player")) {
			if (collisionInfo.gameObject.name == "Player1")
				GameObject.Find ("Player1").GetComponent<PlayerController>().incrementScore(1);
			else if (collisionInfo.gameObject.name == "Player2")
				GameObject.Find ("Player2").GetComponent<PlayerController>().incrementScore(1);

			Destroy(this.gameObject);
		}
	}
}
