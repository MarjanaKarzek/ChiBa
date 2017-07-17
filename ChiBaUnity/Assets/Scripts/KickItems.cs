using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class KickItems : MonoBehaviour {

    public int AddForceMulti = 20;

    private void OnCollisionEnter(Collision collision)
    {
     
        Rigidbody rb = collision.gameObject.GetComponent<Rigidbody>();

        rb.AddForce(Vector3.up * AddForceMulti, ForceMode.Impulse);
    }
}
