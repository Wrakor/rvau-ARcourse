﻿using UnityEngine;
using System.Collections;

public class PadController : MonoBehaviour {
	
	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update() {
		if (Input.GetMouseButton(0)){

			print ("Pressed mouse in position: " + (Input.mousePosition.x-762.0f));


		}
		transform.position = new Vector3 ((Input.mousePosition.x-(Screen.width/2))*0.1f, 0.5f, -15f);
	}
}
