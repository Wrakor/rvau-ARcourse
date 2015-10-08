using UnityEngine;
using System.Collections;

public class BlockController : MonoBehaviour {

	void Start(){
		InitializeColor ();
	}

	public void InitializeColor(){
		GetComponent<Renderer>().material.color = new Color (Random.Range (0.0f, 1.0f), Random.Range (0.0f, 1.0f), Random.Range (0.0f, 1.0f));
	}
	 
}
