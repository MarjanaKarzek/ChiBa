using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/**
 * \author   Jhomar Besmens
 * \version 1.0
 * \date    2017-07-22
 *
 * \brief   This class let the maincamera follow the transformation from the chiba model     
 */
public class ChibaRotateCam : MonoBehaviour {

    public GameObject target; /*!< Variable for the target which will be follow by the camera */

    /**
     * This method update the transform from the chiba model to the camer with the LookAt() function
     */
    private void LateUpdate()
    {
        transform.LookAt(target.transform);
    }
}
