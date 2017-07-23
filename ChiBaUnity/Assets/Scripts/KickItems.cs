using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-19
 *
 * \brief   This class makes the collider work with other objects collider and everytime if the player hit one of these they get push away.   
 */
public class KickItems : MonoBehaviour {

    public int ForceMultipliedBy = 20; /*!< Variable for the force intensity */

    /**
     * This method detects the collision by the other gameobject and add the force to it
     * \param[in]   collision   The detected collison
     */
    private void OnCollisionEnter(Collision collision)
    {
        Rigidbody rb = collision.gameObject.GetComponent<Rigidbody>();

        rb.AddForce(Vector3.up * ForceMultipliedBy, ForceMode.Impulse);
    }
}
