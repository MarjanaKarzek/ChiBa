using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class creates a foward movement with the chiba avatar if the user looks down with the VR-Device      
 */
public class VRLookAndWalk : MonoBehaviour {

    public Transform vrCamera;  /*!< Variable for the Camera */
    public float toggleAngle = 30.0f; /*!< Variable for the toggle angle which trigger the forward movement */
    public float speed = 50.0f; /*!< Variable for the movement speed */
    public bool moveForward;    /*!< Variable which checks if moveForward is true or false */

    private CharacterController cc; /*!< Variable for the CharacterController component */
    
    /**
     * This method takes the CharacterController component
     */
	void Start () {
        cc = GetComponent<CharacterController>();
	}
	
    /**
     * This method checks the requiered angle for the movement and set it to true or false. 
     * Then get the forward transform direction of the vrCamera and calls Unitys SimpleMove() method multiply by speed.
     */
	void Update () {
		if(vrCamera.eulerAngles.x >= toggleAngle && vrCamera.eulerAngles.x < 90.0f)
        {
            moveForward = true;
        }
        else
        {
            moveForward = false;
        }

        if (moveForward)
        {
            Vector3 forward = vrCamera.TransformDirection(Vector3.forward);

            cc.SimpleMove(forward * speed);
        }
	}
}
