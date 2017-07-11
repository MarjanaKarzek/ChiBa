using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PickableItem : MonoBehaviour {

    public InventoryItem inventoryItem;
    private Inventory inventory;
    private Transform player;

    private void Start()
    {
        player = GameObject.FindGameObjectWithTag("Player").transform;
        inventory = player.GetComponent<Inventory>();

        //inventory.AddItem(inventoryItem);
    
    }

    //Needs to re-configure VR
    public void OnClickToInventory()
    {
        if (inventory.AddItem(inventoryItem))
        {
            if (inventoryItem.picSound != null)
                AudioSource.PlayClipAtPoint(inventoryItem.picSound, player.position);

            Destroy(gameObject);
        }
    }

}
