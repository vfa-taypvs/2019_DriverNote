//
//  XuphatCell.swift
//  CamNangNguoiLaiXe
//
//  Created by Hung Le on 3/5/17.
//  Copyright © 2017 Hung Le. All rights reserved.
//

import UIKit

class XuphatCell: UITableViewCell {

    var viewMain: UIView?
    var img: UIImageView?
    var lbl: UILabel?
    
    override init(style: UITableViewCellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup() {
        viewMain = UIView(frame: CGRect(x: 15, y: 0, w: self.w - 30, h: self.h))
        viewMain?.backgroundColor = .white
        self.addSubview(viewMain!)
        
        img = UIImageView(frame: CGRect(x: 15, y: 0, w: 45, h: 45))
        img?.image = #imageLiteral(resourceName: "icon_document")
        viewMain?.addSubview(img!)
        
        lbl = UILabel(frame: CGRect(x: (img?.x)! + (img?.w)! + 15, y: 0, w: 30, h: 50))
        lbl?.numberOfLines = 2
        lbl?.lineBreakMode = .byWordWrapping
        lbl?.font = UIFont.systemFont(ofSize: 16)
        viewMain?.addSubview(lbl!)
    }
    
    func updateData() {
        viewMain?.frame = CGRect(x: (viewMain?.x)!, y: 0, w: self.w - (viewMain?.x)! * 2, h: self.h)
        img?.centerY = (viewMain?.centerY)!
        lbl?.frame = CGRect(x: (lbl?.x)!, y: 0, w: (viewMain?.w)! - (lbl?.x)! * 2, h: (lbl?.getEstimatedHeight())!)
        lbl?.centerY = (viewMain?.centerY)!
    }

}
