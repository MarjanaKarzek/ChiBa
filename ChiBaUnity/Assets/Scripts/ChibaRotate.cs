using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class rotates the chiba model at the current position to right or left     
 */
public class ChibaRotate : MonoBehaviour {

    public GameObject camera;   /*!< Variable for the camera */
    public Quaternion rotateChiba = Quaternion.identity;    /*!< Variable for the identity rotation from the chiba model in the world axes */

	/**
     * This method checks if the euler angles has changed and update them to the rotation identity from the chiba model.
     */
	void Update () {
        rotateChiba.eulerAngles = new Vector3(transform.rotation.eulerAngles.x, camera.transform.rotation.eulerAngles.y, transform.rotation.eulerAngles.x);
        transform.rotation = rotateChiba;

    }
}
