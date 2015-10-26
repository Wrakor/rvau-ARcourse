using UnityEngine;
using System.Collections;

public class MiniballController : MonoBehaviour {

	void OnCollisionEnter(Collision collisionInfo) {

		if (collisionInfo.gameObject.CompareTag ("EndWall")) {
			Destroy(this.gameObject);
		}
	}
}
