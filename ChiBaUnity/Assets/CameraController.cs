using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraController : MonoBehaviour {

    public Transform target;
    private Vector3 offset;

	void Start () {
        offset = target.position - transform.position;
		
	}
	
	// Update is called once per frame
	void LateUpdate () {
        transform.position = target.position - offset;
	}
}
