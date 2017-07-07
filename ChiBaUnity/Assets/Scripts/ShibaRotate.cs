using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ShibaRotate : MonoBehaviour {

    public GameObject kamera;
    public Quaternion rotateShiba = Quaternion.identity;

	
	void Update () {
        rotateShiba.eulerAngles = new Vector3(transform.rotation.eulerAngles.x, kamera.transform.rotation.eulerAngles.y, transform.rotation.eulerAngles.x);
        transform.rotation = rotateShiba;

    }
}
